package com.longbridge.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SendEmailAsync;
import com.longbridge.Util.ShippingUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.respbodydto.OrderDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.services.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Longbridge on 03/01/2018.
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ArtWorkPictureRepository artWorkPictureRepository;

    @Autowired
    MaterialPictureRepository materialPictureRepository;

    @Autowired
    ProductPictureRepository productPictureRepository;

    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    RefundRepository refundRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;

    @Autowired
    CartRepository cartRepository;


    @Autowired
    PocketRepository pocketRepository;

    @Autowired
    ProductSizesRepository productSizesRepository;


    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PocketService pocketService;

    @Autowired
    PaymentService paymentService;


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AddressRepository addressRepository;


    @Autowired
    GeneralUtil generalUtil;


    @Autowired
    ShippingUtil shippingUtil;

    @Autowired
    ProductAttributeRepository productAttributeRepository;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    WalletService walletService;


    @Transactional
    @Override
    public PaymentResponse addOrder(OrderReqDTO orderReq) {
        User user = getCurrentUser();
        PaymentResponse orderRespDTO = new PaymentResponse();
        try{
            Orders orders = new Orders();
            Double totalAmount = 0.0;
            Double totalShippingAmount = 0.0;
            Date date = new Date();
            String orderNumber = "";
            if(orderReq.getItems().size() <1){
                orderRespDTO.setStatus("noitems");
                return orderRespDTO;
            }
            List<ProductSizes> productSizes = new ArrayList<ProductSizes>();

            for (Items items: orderReq.getItems()) {
                if(items.getProductAttributeId() != null){
                    ProductAttribute itemAttribute = productAttributeRepository.findOne(items.getProductAttributeId());

                    if(itemAttribute != null){
                        ProductSizes sizes = productSizesRepository.findByProductAttributeAndName(itemAttribute, items.getSize());
                        if(items.getMeasurementId() == null){
                            if(sizes.getNumberInStock() < items.getQuantity()){
                                orderRespDTO.setStatus("false");
                                return orderRespDTO;
                            }
                            sizes.setNumberInStock(sizes.getNumberInStock() - items.getQuantity());
                            productSizes.add(sizes);
                        }else{
                            if(thresholdExceeded(items)){
                                orderRespDTO.setStatus("thresholdLimit");
                                return orderRespDTO;
                            }
                        }
                    }
                }
            }

//            if(orderReq.getPaymentType().equalsIgnoreCase("Wallet")){
//                if(!walletService.validateWalletBalance(totalAmount).equalsIgnoreCase("true")){
//                    orderRespDTO.setStatus("walletchargeerror");
//                    return orderRespDTO;
//                }
//            }

            do{
                orderNumber = generateOrderNum();
            }while (orderNumExists(orderNumber));

            productSizesRepository.save(productSizes);
            orders.setOrderNum(orderNumber);
            orders.setCreatedOn(date);
            orders.setUpdatedOn(date);
            orders.setUserId(user.id);
            orders.setOrderDate(date);
            orders.setPaymentType(orderReq.getPaymentType());
            orders.setPaidAmount(orderReq.getPaidAmount());
            orders.setDeliveryAddress(addressRepository.findOne(orderReq.getDeliveryAddressId()));

            ItemStatus itemStatus = null;
            if(orderReq.getPaymentType().equalsIgnoreCase("CARD_PAYMENT")){
               itemStatus = itemStatusRepository.findByStatus("NV");
               orders.setDeliveryStatus("NV");
            }
            else if(orderReq.getPaymentType().equalsIgnoreCase("BANK_TRANSFER")){
               itemStatus = itemStatusRepository.findByStatus("P");
               orders.setDeliveryStatus("P");
            }

            else if(orderReq.getPaymentType().equalsIgnoreCase("WALLET")){
                itemStatus = itemStatusRepository.findByStatus("P");
                orders.setDeliveryStatus("P");
            }

            orderRepository.save(orders);
            HashMap h= saveItems(orderReq,date,orders,itemStatus);
            totalAmount = Double.parseDouble(h.get("totalAmount").toString());
            totalShippingAmount = Double.parseDouble(h.get("totalShippingAmount").toString());
            orders.setTotalAmount(totalAmount);
            orders.setShippingAmount(totalShippingAmount);
            pocketService.updatePocketForOrderPayment(user,Double.parseDouble(h.get("totalAmount").toString()),orderReq.getPaymentType());

            if(orderReq.getPaymentType().equalsIgnoreCase("CARD_PAYMENT")){
                PaymentRequest paymentRequest = new PaymentRequest();
                paymentRequest.setOrderId(orders.id);
                paymentRequest.setTransactionAmount(totalAmount);
                paymentRequest.setTransactionReference(orderNumber);
                paymentRequest.setEmail(user.getEmail());
                paymentRepository.save(paymentRequest);
                return paymentService.initiatePayment(paymentRequest);
            }


            orderRepository.save(orders);
            deleteCart(user);
            sendEmailAsync.sendEmailToUser(user,orderNumber);
            orderRespDTO.setStatus("00");
            orderRespDTO.setOrderNumber(orderNumber);
            return orderRespDTO;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }





    private void updateOrder(Orders orders, User user) {
        ItemStatus itemStatus = itemStatusRepository.findByStatus("P");
        for (Items item:orders.getItems()) {
            item.setItemStatus(itemStatus);
            itemRepository.save(item);
        }
        orders.setDeliveryStatus("P");
        sendEmailAsync.sendEmailToUser(user, orders.getOrderNum());

    }

    @Override
    public Boolean thresholdExceeded(Items items){

        int threshold = designerRepository.findById(items.getDesignerId()).getThreshold();
        int activeOrders = itemRepository.countByDesignerIdAndDeliveryStatusIn(items.getDesignerId(), Arrays.asList("A", "PC", "OP", "CO", "RI", "OS", "WR", "WC", "P"));
        int maxQuantity = threshold - activeOrders;

        if(items.getQuantity() > maxQuantity){
            return true;
        }else{
            return false;
        }
    }

    private HashMap saveItems(OrderReqDTO orderReq,Date date,Orders orders,ItemStatus itemStatus) {
        Double totalAmount=0.0;
        Double shippingAmount = 0.0;
        Double totalShippingAmount = 0.0;
        List<String> designerCities = new ArrayList<>();
        List<DesignerOrderDTO> designerDTOS = new ArrayList<>();
        for (Items items: orderReq.getItems()) {
            Products p = productRepository.findOne(items.getProductId());
            if(items.getMeasurementId() != null) {
                Measurement measurement = measurementRepository.findOne(items.getMeasurementId());
                try {
                ObjectMapper mapper = new ObjectMapper();
                String saveMeasurement=mapper.writeValueAsString(measurement);
                    items.setMeasurement(saveMeasurement);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();

                }
            }
            if(items.getArtWorkPictureId() != null){
                items.setArtWorkPicture(artWorkPictureRepository.findOne(items.getArtWorkPictureId()).getPictureName());
            }
            if(items.getMaterialPictureId() != null){
                items.setMaterialPicture(materialPictureRepository.findOne(items.getMaterialPictureId()).getPictureName());
            }

            items.setProductPicture(productPictureRepository.findFirst1ByProducts(p).getPictureName());

            Double amount;
            if(p.getPriceSlash() != null && p.getPriceSlash().getSlashedPrice() > 0){
                amount = p.getPriceSlash().getSlashedPrice();
            }else {
                amount = p.getAmount();
            }
            Double itemsAmount = amount*items.getQuantity();
            if(!designerCities.contains(p.getDesigner().getCity().toUpperCase().trim())){
                shippingAmount = shippingUtil.getShipping(p.getDesigner().getCity().toUpperCase().trim(), orders.getDeliveryAddress().getCity().toUpperCase().trim(), items.getQuantity());
                designerCities.add(p.getDesigner().getCity().toUpperCase().trim());
           }
            items.setAmount(itemsAmount);
            totalShippingAmount = totalShippingAmount+shippingAmount;
            totalAmount=totalAmount+itemsAmount+shippingAmount;
            items.setOrders(orders);
            items.setProductName(p.getName());
            items.setCreatedOn(date);
            items.setUpdatedOn(date);
            items.setItemStatus(itemStatus);
            itemRepository.save(items);
            p.setNumOfTimesOrdered(p.getNumOfTimesOrdered()+1);
            if(items.getMeasurement() == null) {
                if (p.getStockNo() != 0) {
                    p.setStockNo(p.getStockNo() - items.getQuantity());
                   // ProductSizes productSizes = productSizesRepository.findByProductsAndName(p, items.getSize());
                    //productSizes.setStockNo(productSizes.getStockNo() - items.getQuantity());
                    //productSizesRepository.save(productSizes);

                } else {
                    p.setInStock("N");
                }

                if (p.getStockNo() == 0) {
                    p.setInStock("N");
                }
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
        hm.put("totalShippingAmount", totalShippingAmount);

        return hm;
    }

    @Override
    public List<StatusMessageDTO> updateOrderItemByDesignerr(ItemsDTO itemsDTO, User user) {
        ItemStatus itemStatus = itemStatusRepository.findOne(itemsDTO.getStatusId());
        Items item = itemRepository.findOne(itemsDTO.getId());

        if(item != null){
            if(itemStatus.getStatusMessages().size() > 0){
                List<StatusMessageDTO> statusMessageDTOS = new ArrayList<StatusMessageDTO>();

                for(StatusMessage sdt : itemStatus.getStatusMessages()){
                    StatusMessageDTO sd = new StatusMessageDTO();
                    sd.id = sdt.id;
                    sd.statusId = sdt.getItemStatus().id;
                    sd.message = sdt.getMessage();
                    statusMessageDTOS.add(sd);
                }

                return statusMessageDTOS;
            }else{
                item.setItemStatus(itemStatus);
                itemRepository.save(item);
            }
        }

        return null;
    }

    @Transactional
    @Override
    public void userRejectDecision(ItemsDTO itemsDTO) {
        try{
            User user=getCurrentUser();
            Items items = itemRepository.findOne(itemsDTO.getId());

            if (items == null) {
                throw new WawoohException();
            }else {
                Pocket pocket = pocketRepository.findByUser(user);
                ItemStatus itemStatus = itemStatusRepository.findByStatus("OR");
                if(items.getItemStatus() != itemStatus){
                    throw new WawoohException();
                }
                if (itemsDTO.getAction().equalsIgnoreCase("accept")) {
                    items.setItemStatus(itemStatusRepository.findByStatus("PC"));
                } else if (itemsDTO.getAction().equalsIgnoreCase("refund")) {
                    pocket.setBalance(pocket.getBalance() - items.getAmount());
                    pocket.setPendingSettlement(pocket.getPendingSettlement() - items.getAmount());
                    pocketRepository.save(pocket);
                    Refund refund = new Refund();
                    refund.setAccountName(itemsDTO.getAccountName());
                    refund.setAccountNumber(itemsDTO.getAccountNumber());
                    refund.setAmount(items.getAmount());
                    refund.setCustomerName(user.getLastName()+" "+user.getFirstName());
                    refund.setOrderNum(items.getOrders().getOrderNum());
                    refund.setProductName(items.getProductName());
                    refundRepository.save(refund);
                    items.setItemStatus(itemStatusRepository.findByStatus("C"));

                } else if (itemsDTO.getAction().equalsIgnoreCase("shopanother")) {
                    pocket.setPendingSettlement(pocket.getPendingSettlement() - items.getAmount());
                    pocketRepository.save(pocket);
                }
                itemRepository.save(items);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }

    @Override
    public List<Orders> getOrdersByUser() {
        try {
            User user=getCurrentUser();
            List<Orders> orders= orderRepository.findByUserId(user.id);
            return orders;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public String addToCart(Cart cart) {
        try{
            User user = getCurrentUser();
            Date date = new Date();
            if(cart.getMaterialLocation() != null){
                Address address=new Address();
                BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
                BeanUtils.copyProperties(address,cart.getMaterialLocation());
                address.setUser(user);
                addressRepository.save(address);
                cart.setMaterialLocation(address);

            }else if(cart.getMaterialPickUpAddressId() != null){
            cart.setMaterialLocation(addressRepository.findOne(cart.getMaterialPickUpAddressId()));
            }
            Products products = productRepository.findOne(cart.getProductId());
            Double amount;
            if(products.getPriceSlash() != null && products.getPriceSlash().getSlashedPrice()>0){
                amount=products.getAmount()-products.getPriceSlash().getSlashedPrice();
            }else {
                amount=products.getAmount();
            }

            cart.setAmount(amount*cart.getQuantity());
            //cart.setAmount(newAmount);
            cart.setCreatedOn(date);
            cart.setUpdatedOn(date);
            cart.setExpiryDate(DateUtils.addDays(date,7));
            cart.setUser(user);
            cartRepository.save(cart);
            String carts=cartRepository.countByUser(user).toString();
            return carts;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public String updateCart(Cart cart) {
        try{
            Date date = new Date();
            Cart cartTemp = cartRepository.findOne(cart.id);
            double amount;
            Products products = productRepository.findOne(cartTemp.getProductId());
            if(products.getPriceSlash() != null && products.getPriceSlash().getSlashedPrice()>0){
                amount = products.getPriceSlash().getSlashedPrice();
            }else {
                amount = products.getAmount();
            }

            int qty = cart.getQuantity();
            cartTemp.setQuantity(cart.getQuantity());
            Double newAmount = amount*qty;
            cartTemp.setAmount(newAmount);
            cartTemp.setUpdatedOn(date);
            cartTemp.setExpiryDate(DateUtils.addDays(date,7));
            cartTemp.setUser(getCurrentUser());
            cartRepository.save(cartTemp);
           return "success";

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public String addItemsToCart(CartListDTO carts) {
        try{
            Date date = new Date();

            for (Cart cart: carts.getCarts()) {
                cart.setCreatedOn(date);
                cart.setUpdatedOn(date);
                cart.setExpiryDate(DateUtils.addDays(date,7));
                cart.setUser(getCurrentUser());
                cartRepository.save(cart);
            }

            return cartRepository.countByUser(getCurrentUser()).toString();


        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public UserCartDTO getCarts() {
        try {
            List<Cart> carts= cartRepository.findByUser(getCurrentUser());
            List<CartDTO> cartDTOS = generalUtil.convertCartEntsToDTOs(carts);
            Double totalPrice = 0.0;

            for (CartDTO cartDTO : cartDTOS) {
                totalPrice += cartDTO.getTotalPrice();
            }

            UserCartDTO userCartDTO = new UserCartDTO();
            userCartDTO.setCartItems(cartDTOS);
            userCartDTO.setTotalPrice(totalPrice);

            return userCartDTO;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteCart(Long id) {
        try {

            cartRepository.delete(id);

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void emptyCart() {
        try {
        List<Cart> carts = cartRepository.findByUser(getCurrentUser());
        cartRepository.delete(carts);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public OrderDTO getOrdersById(Long id) {
        try {

            return generalUtil.convertOrderEntToDTOs(orderRepository.findOne(id));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public OrderDTO getOrdersByOrderNum(String orderNumber) {
        try {

            return generalUtil.convertOrderEntToDTOs(orderRepository.findByOrderNum(orderNumber));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public ItemsRespDTO getOrderItemById(Long id) {
        try {

            return generalUtil.convertEntityToDTO(itemRepository.findOne(id));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void saveUserOrderDecision(ItemsDTO itemsDTO) {
        try {
            User user = getCurrentUser();
            if(itemsDTO.getId() != null){
                Items items = itemRepository.findOne(itemsDTO.getId());

                if(items.getItemStatus().getStatus().equalsIgnoreCase("OR")){
                    if(itemsDTO.getAction().equalsIgnoreCase("A")){
                        items.setItemStatus(itemStatusRepository.findByStatus("PC"));
                    }
                    else if(itemsDTO.getAction().equalsIgnoreCase("R")){
                        items.setItemStatus(itemStatusRepository.findByStatus("C"));
                        Refund refund = new Refund();
                        refund.setAccountName(itemsDTO.getAccountName());
                        refund.setAccountNumber(itemsDTO.getAccountNumber());
                        refund.setAmount(items.getAmount());
                        refund.setUserId(user.id);
                        refundRepository.save(refund);

                    }
                }
                itemRepository.save(items);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void saveUserOrderComplain(ItemsDTO itemsDTO) {
        try {
            if(itemsDTO.getId() != null) {
                Items items = itemRepository.findOne(itemsDTO.getId());
                if (items.getItemStatus().getStatus().equalsIgnoreCase("D")) {
                    items.setComplain(itemsDTO.getComplain());
                    itemRepository.save(items);
                }
            }
            }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public String validateItemQuantity(List<Items> item){
        List<ProductSizes> productSizes = new ArrayList<ProductSizes>();
        String status = "";

        for (Items items: item) {
            if(items.getProductAttributeId() != null){
                ProductAttribute itemAttribute = productAttributeRepository.findOne(items.getProductAttributeId());

                if(itemAttribute != null){
                    ProductSizes sizes = productSizesRepository.findByProductAttributeAndName(itemAttribute, items.getSize());
                    if(items.getMeasurementId() == null){
                        if(sizes.getNumberInStock() < items.getQuantity()){
                            status = "false";
                        }
                        sizes.setNumberInStock(sizes.getNumberInStock() - items.getQuantity());
                        productSizes.add(sizes);
                    }else{
                        if(thresholdExceeded(items)){
                            status = "thresholdLimit";
                        }
                    }
                }
            }
        }

        if(status.equalsIgnoreCase("")){
            productSizesRepository.save(productSizes);
        }
        return status;
    }


    @Override
    public Boolean orderNumExists(String orderNum) {
        Orders orders = orderRepository.findByOrderNum(orderNum);
        return (orders != null) ? true : false;
    }


    private String generateOrderNum(){
        Random r = new Random(System.currentTimeMillis() );
        int random = 1000000000 + r.nextInt(9999999);
        String orderNum = Integer.toString(random);
        return orderNum;
    }

    @Override
    public PaymentResponse cardPayment(Orders orders, String email){
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(orders.id);
        paymentRequest.setTransactionAmount(orders.getTotalAmount());
        paymentRequest.setTransactionReference(orders.getOrderNum());
        paymentRequest.setEmail(email);
        paymentRepository.save(paymentRequest);
        try {
            return paymentService.initiatePayment(paymentRequest);
        } catch (UnirestException e) {
            throw new WawoohException();
        }
    }

    private void deleteCart(User user){
        System.out.println( "cart repo"+ cartRepository.findByUser(user));
        List<Cart> carts = cartRepository.findByUser(user);
        cartRepository.delete(carts);

    }

}
