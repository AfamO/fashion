package com.longbridge.services.implementations;

import com.longbridge.Util.SendEmailAsync;
import com.longbridge.Util.ShippingUtil;
import com.longbridge.dto.DesignerOrderDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.services.AnonymousOrderService;
import com.longbridge.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnonymousOrderServiceImpl implements AnonymousOrderService {

    @Autowired
    AnonymousUserRepository anonymousUserRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPictureRepository productPictureRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;

    @Autowired
    OrderService orderService;

    @Autowired
    ShippingUtil shippingUtil;

    @Override
    public PaymentResponse addOrder(OrderReqDTO orderReq) {

        PaymentResponse orderRespDTO = new PaymentResponse();
        AnonymousUser user = anonymousUserRepository.findOne(orderReq.getAnonymousUser().id);
        User tempUser = new User();
        tempUser.setEmail(user.getEmail());
        tempUser.setPhoneNo(user.getPhoneNo());

        try{
            Orders orders = new Orders();
            Double totalAmount = 0.0;
            Date date = new Date();
            String orderNumber = "";
            if(orderReq.getItems().size() <1){
                orderRespDTO.setStatus("noitems");
                return orderRespDTO;
            }


            String itemValidationStatus = orderService.validateItemQuantity(orderReq.getItems());
            if(itemValidationStatus.equalsIgnoreCase("false")){
                orderRespDTO.setStatus("false");
                return orderRespDTO;
            }else if(itemValidationStatus.equalsIgnoreCase("thresholdLimit")){
                orderRespDTO.setStatus("thresholdLimit");
                return orderRespDTO;
            }

            do{
                orderNumber = generateOrderNum();
            }while (orderService.orderNumExists(orderNumber));

            orders.setOrderNum(orderNumber);
            orders.setCreatedOn(date);
            orders.setUpdatedOn(date);
            orders.setOrderDate(date);
            orders.setPaymentType(orderReq.getPaymentType());
            orders.setPaidAmount(orderReq.getPaidAmount());
            orders.setAnonymousBuyer(true);
            orders.setAnonymousUserId(user.id);

            ItemStatus itemStatus = null;
            if(orderReq.getPaymentType().equalsIgnoreCase("Card Payment")){
                itemStatus = itemStatusRepository.findByStatus("NV");
                orders.setDeliveryStatus("NV");
            }
            else if(orderReq.getPaymentType().equalsIgnoreCase("Bank Transfer")){
                itemStatus = itemStatusRepository.findByStatus("P");
                orders.setDeliveryStatus("P");
            }


            HashMap h = saveItems(orderReq,date,orders,itemStatus);
            totalAmount = Double.parseDouble(h.get("totalAmount").toString());
            orders.setTotalAmount(totalAmount);

            if(orderReq.getPaymentType().equalsIgnoreCase("Card Payment")){
                return orderService.cardPayment(orders, user.getEmail());
            }

            orderRepository.save(orders);
            sendEmailAsync.sendEmailToUser(tempUser,orderNumber);
            orderRespDTO.setStatus("00");
            orderRespDTO.setOrderNumber(orderNumber);
            return orderRespDTO;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    private String generateOrderNum(){

        Random r = new Random(System.currentTimeMillis() );
        int random = 1000000000 + r.nextInt(9999999);
        String orderNum = Integer.toString(random);
        return orderNum;
    }

    private HashMap saveItems(OrderReqDTO orderReq,Date date,Orders orders,ItemStatus itemStatus) {
        Double totalAmount=0.0;
        Double amountWithoutShipping=0.0;
        Double shippingAmount = 0.0;
        List<String> designerCities = new ArrayList<>();
        List<DesignerOrderDTO> designerDTOS = new ArrayList<>();
        for (Items items: orderReq.getItems()) {
            Products p = productRepository.findOne(items.getProductId());
            items.setProductPicture(productPictureRepository.findFirst1ByProducts(p).getPictureName());

            Double amount;
            if(p.getPriceSlash() != null && p.getPriceSlash().getSlashedPrice() > 0){
                amount = p.getPriceSlash().getSlashedPrice();
            }else {
                amount = p.getAmount();
            }

            Double itemsAmount = amount*items.getQuantity();
            if(!designerCities.contains(p.getDesigner().getCity().toUpperCase().trim())){
                AnonymousUser anonymousUser = anonymousUserRepository.findOne(orders.getAnonymousUserId());
                shippingAmount = shippingUtil.getShipping(p.getDesigner().getCity().toUpperCase().trim(), anonymousUser.getCity().toUpperCase().trim(), items.getQuantity());
                designerCities.add(p.getDesigner().getCity().toUpperCase().trim());
            }

            amountWithoutShipping = itemsAmount;
            items.setAmount(itemsAmount);
            totalAmount += itemsAmount+shippingAmount;
            items.setOrders(orders);
            items.setProductName(p.getName());
            items.setCreatedOn(date);
            items.setUpdatedOn(date);
            items.setItemStatus(itemStatus);
            itemRepository.save(items);
            p.setNumOfTimesOrdered(p.getNumOfTimesOrdered()+1);
            if (p.getStockNo() != 0) {
                p.setStockNo(p.getStockNo() - items.getQuantity());
            } else {
                p.setInStock("N");
            }

            if (p.getStockNo() == 0) {
                p.setInStock("N");
            }

            productRepository.save(p);
            DesignerOrderDTO dto= new DesignerOrderDTO();
            dto.setProductName(p.getName());
            dto.setStoreName(p.getDesigner().getStoreName());
            dto.setDesignerEmail(p.getDesigner().getUser().getEmail());
            designerDTOS.add(dto);
            sendEmailAsync.sendEmailToDesigner(designerDTOS,orders.getOrderNum());
        }


        HashMap hm = new HashMap();
        hm.put("totalAmount", totalAmount);
        hm.put("amountWithoutShipping", amountWithoutShipping);

        return hm;
    }
}
