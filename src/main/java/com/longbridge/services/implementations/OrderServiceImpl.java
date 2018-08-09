package com.longbridge.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SendEmailAsync;
import com.longbridge.dto.*;
import com.longbridge.exception.AppException;
import com.longbridge.exception.InvalidStatusUpdateException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.respbodydto.OrderDTO;
import com.longbridge.respbodydto.OrderRespDTO;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.MailService;
import com.longbridge.services.OrderService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.Format;
import java.text.SimpleDateFormat;
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
    StatusMessageRepository statusMessageRepository;

    @Autowired
    RefundRepository refundRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;


    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductSizesRepository productSizesRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MailService mailService;

    @Autowired
    RavePaymentRepository ravePaymentRepository;

    @Autowired
    WalletRepository walletRepository;

    private Locale locale = LocaleContextHolder.getLocale();


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    TransferInfoRepository transferInfoRepository;

    @Autowired
    GeneralUtil generalUtil;


    @Transactional
    @Override
    public OrderRespDTO addOrder(OrderReqDTO orderReq, User user) {
        OrderRespDTO orderRespDTO = new OrderRespDTO();
        try{

            Orders orders = new Orders();
            Double totalAmount = 0.0;
            Date date = new Date();
            String orderNumber = "";

            if(orderReq.getItems().size() <1){
                orderRespDTO.setStatus("noitems");
                return orderRespDTO;
            }
            for (Items items: orderReq.getItems()) {
                Products p = productRepository.findOne(items.getProductId());
                if(p.stockNo == 0 && items.getMeasurementId() == null ){
                    orderRespDTO.setStatus("false");
                    return orderRespDTO;
                }
            }


            while (!orderNumExists(generateOrderNum())){
                orderNumber = "WAW#"+generateOrderNum();
                orders.setOrderNum(orderNumber);
                break;
            }
            orders.setCreatedOn(date);
            orders.setUpdatedOn(date);
            orders.setUserId(user.id);
            orders.setOrderDate(date);
            orders.setPaymentType(orderReq.getPaymentType());
            orders.setPaidAmount(orderReq.getPaidAmount());
            orders.setDeliveryAddress(addressRepository.findOne(orderReq.getDeliveryAddressId()));
            orderRepository.save(orders);

            ItemStatus itemStatus = new ItemStatus();
            if(orderReq.getPaymentType().equalsIgnoreCase("Card Payment")){
               itemStatus = itemStatusRepository.findByStatus("NV");
               orders.setDeliveryStatus("NV");
            }
            else if(orderReq.getPaymentType().equalsIgnoreCase("Bank Transfer")){
               itemStatus = itemStatusRepository.findByStatus("P");
               orders.setDeliveryStatus("P");
            }
            else if(orderReq.getPaymentType().equalsIgnoreCase("Wallet")){
                itemStatus=itemStatusRepository.findByStatus("PC");
                orders.setDeliveryStatus("PC");

            }
            HashMap h= saveItems(orderReq,date,orders,itemStatus);
            totalAmount=Double.parseDouble(h.get("totalAmount").toString());
            orders.setTotalAmount(totalAmount);
            orderRepository.save(orders);
            updateWalletForOrderPayment(user,Double.parseDouble(h.get("amountWithoutShipping").toString()),orderReq.getPaymentType());

            if(orderReq.getPaymentType().equalsIgnoreCase("Card Payment")){
                //generate a unique ref number
                String trnxRef = "WAW"+ "-"+ generateOrderNum();
                RavePayment ravePayment = new RavePayment();
                ravePayment.setOrderId(orders.id);
                ravePayment.setTransactionAmount(totalAmount);
                ravePayment.setTransactionReference(trnxRef);
                ravePaymentRepository.save(ravePayment);
                orderRespDTO.setOrderNumber(orderNumber);
                orderRespDTO.setTransactionReference(trnxRef);
                orderRespDTO.setTotalAmount(totalAmount);
                orderRespDTO.setId(orders.id);
                orderRespDTO.setStatus("00");
                return orderRespDTO;
            }

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

    private HashMap saveItems(OrderReqDTO orderReq,Date date,Orders orders,ItemStatus itemStatus) {
        Double totalAmount=0.0;
        Double amountWithoutShipping=0.0;
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
                items.setArtWorkPicture(artWorkPictureRepository.findOne(items.getArtWorkPictureId()).pictureName);
            }
            if(items.getMaterialPictureId() != null){
                items.setMaterialPicture(materialPictureRepository.findOne(items.getMaterialPictureId()).pictureName);
            }

            items.setProductPicture(productPictureRepository.findFirst1ByProducts(p).pictureName);

            Double amount;
            if(p.priceSlash != null && p.priceSlash.getSlashedPrice()>0){
                amount=p.amount-p.priceSlash.getSlashedPrice();
            }else {
                amount=p.amount;
            }

            Double itemsAmount = amount*items.getQuantity();
            Double shippingAmount = generalUtil.getShipping(p.designer.city.toUpperCase().trim(),orders.getDeliveryAddress().getCity().toUpperCase().trim(),items.getQuantity());
            amountWithoutShipping=itemsAmount;
            items.setAmount(itemsAmount);
            totalAmount=totalAmount+itemsAmount+shippingAmount;
            items.setOrders(orders);
            items.setProductName(p.name);
            items.setCreatedOn(date);
            items.setUpdatedOn(date);
            items.setItemStatus(itemStatus);
            itemRepository.save(items);


            p.numOfTimesOrdered = p.numOfTimesOrdered+1;


            if(items.getMeasurement() == null) {
                if (p.stockNo != 0) {
                    p.stockNo = p.stockNo - items.getQuantity();
                    ProductSizes productSizes = productSizesRepository.findByProductsAndName(p, items.getSize());
                    productSizes.setStockNo(productSizes.getStockNo() - items.getQuantity());
                    productSizesRepository.save(productSizes);

                } else {
                    p.inStock = "N";
                }

                if (p.stockNo == 0) {
                    p.inStock = "N";
                }
            }

            productRepository.save(p);


        }

        HashMap hm = new HashMap();
        hm.put("totalAmount", totalAmount);
        hm.put("amountWithoutShipping", amountWithoutShipping);

        return hm;
    }


    //todo later
    @Override
    public void updateOrderItemByDesignerWithMessage(ItemsDTO itemsDTO, User user) {
        try{
            ItemsDTO itemsDTO1 = new ItemsDTO();
            Date date = new Date();
            User customer = userRepository.findOne(itemsDTO.getCustomerId());
            Items items = itemRepository.findOne(itemsDTO.getId());
            String customerEmail = customer.email;
            String rejectDecisionLink;
            String customerName = customer.lastName+" "+ customer.firstName;
//            Context context = new Context();
//            context.setVariable("name", customerName);
//            context.setVariable("productName",items.getProductName());
            Context context = new Context();
            context.setVariable("name", customerName);
            context.setVariable("productName",items.getProductName());

            Products products = productRepository.findOne(items.getProductId());
            ItemStatus itemStatus = itemStatusRepository.findOne(itemsDTO.getStatusId());
            StatusMessage statusMessage = statusMessageRepository.findOne(itemsDTO.getMessageId());


            if(items.getItemStatus().getStatus().equalsIgnoreCase("PC")) {

                    if (itemsDTO.getStatus().equalsIgnoreCase("OP")) {
                        items.setItemStatus(itemStatus);
                        //items.setStatusMessage(statusMessage);
                    }

                    else if (itemsDTO.getStatus().equalsIgnoreCase("RI")) {
                        items.setItemStatus(itemStatus);

                        String message = templateEngine.process("readyforinsptemplate", context);
                        mailService.prepareAndSend(message,customerEmail,messageSource.getMessage("order.inspection.subject", null, locale));

                        //items.setStatusMessage(statusMessage);
                    }
               // }

                    else if(itemsDTO.getStatus().equalsIgnoreCase("OR")){
                    String encryptedMail = Base64.getEncoder().encodeToString(customerEmail.getBytes());
                    String link;
                    String message;
                    statusMessage.setHasResponse(true);

                    items.setItemStatus(itemStatus);
                    items.setStatusMessage(statusMessage);

                    context.setVariable("waitTime",itemsDTO.getWaitTime());

                    if(itemsDTO.getMessageId() == 3){
                        link=messageSource.getMessage("order.decline.decision", null, locale);
                        rejectDecisionLink=link+encryptedMail+"&itemId="+items.id+"&orderNum="+itemsDTO.getOrderNumber();
                        context.setVariable("link",rejectDecisionLink);
                        message = templateEngine.process("admindeclineordertemplate", context);
                    }
                    else {
                        link=messageSource.getMessage("order.reject.decision", null, locale);
                        rejectDecisionLink=link+encryptedMail+"&itemId="+items.id+"&orderNum="+itemsDTO.getOrderNumber();
                        context.setVariable("link",rejectDecisionLink);
                        message = templateEngine.process("admincancelordertemplate", context);

                    }
                    itemsDTO1.setDeliveryStatus(items.getItemStatus().getStatus());
                    itemsDTO1.setProductName(products.name);
                    itemsDTO1.setLink(rejectDecisionLink);
                    itemsDTO1.setWaitTime(itemsDTO.getWaitTime());

                    try {

                        mailService.prepareAndSend(message, customerEmail, messageSource.getMessage("order.status.subject", null, locale));
                    }catch(MailException me) {
                        me.printStackTrace();
                        throw new AppException(customerName,customerEmail,messageSource.getMessage("order.status.subject", null, locale),itemsDTO1);

                    }
            }
                else {
                    throw new InvalidStatusUpdateException();
                }
            }

            else if(items.getItemStatus().getStatus().equalsIgnoreCase("OP")){
                if(itemsDTO.getStatus().equalsIgnoreCase("RI")){
                    items.setItemStatus(itemStatus);
                    statusMessage.setHasResponse(false);
                    items.setStatusMessage(statusMessage);
                }
                else {
                    throw new InvalidStatusUpdateException();
                }
            }

                items.setUpdatedOn(date);
                itemRepository.save(items);
                Orders orders = orderRepository.findByOrderNum(itemsDTO.getOrderNumber());
                orders.setUpdatedOn(date);
                orders.setUpdatedBy(user.email);
                orderRepository.save(orders);


        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
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

    @Override
    public void updateOrderItemByAdmin(ItemsDTO itemsDTO, User user) {
        try{
            ItemsDTO itemsDTO1 = new ItemsDTO();
            Items items = itemRepository.findOne(itemsDTO.getId());
            Orders orders=orderRepository.findOne(items.getOrders().id);
            Date date = new Date();
            items.setUpdatedOn(date);


            User customer = userRepository.findOne(itemsDTO.getCustomerId());
            String customerEmail = customer.email;
            String customerName = customer.lastName+" "+ customer.firstName;
            Context context = new Context();
            context.setVariable("name", customerName);
            context.setVariable("productName",items.getProductName());

            try {
                ItemStatus itemStatus = itemStatusRepository.findOne(itemsDTO.getStatusId());

               if(items.getItemStatus().getStatus().equalsIgnoreCase("RI")){
                    if(itemsDTO.getStatus().equalsIgnoreCase("PI")){
                        items.setFailedInspectionReason(null);
                        items.setItemStatus(itemStatusRepository.findByStatus("RS"));

                    }
                    else if(itemsDTO.getStatus().equalsIgnoreCase("FI")){
                            items.setItemStatus(itemStatusRepository.findByStatus("OP"));
                            items.setFailedInspectionReason(itemsDTO.getAction());

                            //todo later, send email to user and designer
                            sendEmailAsync.sendFailedInspEmailToUser(customer,itemsDTO);
                            sendEmailAsync.sendFailedInspToDesigner(itemsDTO);
                        }
                    }


            else if(items.getItemStatus().getStatus().equalsIgnoreCase("RS")){
                if(itemsDTO.getStatus().equalsIgnoreCase("OS")){
                    items.setItemStatus(itemStatus);
                    //items.setStatusMessage(statusMessage);

                    String message = templateEngine.process("ordershippedemail", context);
                    mailService.prepareAndSend(message,customerEmail,messageSource.getMessage("order.shipped.subject", null, locale));

                }
            }

            else if(items.getItemStatus().getStatus().equalsIgnoreCase("OS")){
                if(itemsDTO.getStatus().equalsIgnoreCase("D")){
                    items.setItemStatus(itemStatus);
//update wallet balance by removing item amount  from user wallet since item has been delivered
                    updateWalletForOrderDelivery(items, customer);
//end updating wallet balance
                    String encryptedMail = Base64.getEncoder().encodeToString(customerEmail.getBytes());
                    String link = messageSource.getMessage("order.complain",null, locale);
                    link = link+encryptedMail+"&itemId="+items.id+"&orderNum="+itemsDTO.getOrderNumber();
                    context.setVariable("link",link);
                    String message = templateEngine.process("oderdeliveredemail", context);
                    mailService.prepareAndSend(message,customerEmail,messageSource.getMessage("order.delivered.subject", null, locale));

                }
            }
                //Orders orders = orderRepository.findByOrderNum(itemsDTO.getOrderNumber());
                items.setUpdatedOn(date);
                itemRepository.save(items);
                orders.setUpdatedOn(date);
                orders.setUpdatedBy(user.email);
                orderRepository.save(orders);

                itemsDTO1.setDeliveryStatus(items.getItemStatus().getStatus());
                itemsDTO1.setProductName(itemsDTO.getProductName());
                itemsDTO1.setAction(itemsDTO.getAction());
                itemsDTO1.setLink(messageSource.getMessage("order.complain",null, locale));


            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(customerName,customerEmail,messageSource.getMessage("order.status.subject", null, locale),itemsDTO1);

            }


        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    private void updateWalletForOrderDelivery(Items items, User customer) {
        Wallet wallet = walletRepository.findByUser(customer);
        wallet.setPendingSettlement(wallet.getPendingSettlement()-items.getAmount());
        wallet.setBalance(wallet.getBalance()-items.getAmount());
        walletRepository.save(wallet);
    }


    //pp


    @Transactional
    @Override
    public void userRejectDecision(ItemsDTO itemsDTO, User user) {
        try{
            Items items = itemRepository.findOne(itemsDTO.getId());

            if (items == null) {
                throw new WawoohException();
            }else {
                Wallet wallet = walletRepository.findByUser(user);
                ItemStatus itemStatus = itemStatusRepository.findByStatus("OR");
                if(items.getItemStatus() != itemStatus){
                    throw new WawoohException();
                }
                if (itemsDTO.getAction().equalsIgnoreCase("accept")) {
                    items.setItemStatus(itemStatusRepository.findByStatus("PC"));
                } else if (itemsDTO.getAction().equalsIgnoreCase("refund")) {
                    wallet.setBalance(wallet.getBalance() - items.getAmount());
                    wallet.setPendingSettlement(wallet.getPendingSettlement() - items.getAmount());
                    walletRepository.save(wallet);

                    Refund refund = new Refund();
                    refund.setAccountName(itemsDTO.getAccountName());
                    refund.setAccountNumber(itemsDTO.getAccountNumber());
                    refund.setAmount(items.getAmount());
                    refund.setCustomerName(user.lastName+" "+user.firstName);
                    refund.setOrderNum(items.getOrders().getOrderNum());
                    refund.setProductName(items.getProductName());
                    refundRepository.save(refund);
                    items.setItemStatus(itemStatusRepository.findByStatus("C"));

                } else if (itemsDTO.getAction().equalsIgnoreCase("shopanother")) {
                    wallet.setPendingSettlement(wallet.getPendingSettlement() - items.getAmount());
                    walletRepository.save(wallet);
                }
                itemRepository.save(items);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public String updateOrderByAdmin(OrderReqDTO orderReqDTO, User user) {
        try{
            List<DesignerOrderDTO> dtos = new ArrayList<>();
            User customer = userRepository.findOne(orderReqDTO.getUserId());

            Orders orders=orderRepository.findOne(orderReqDTO.getId());
            Date date = new Date();
            orders.setUpdatedOn(date);

            if(orders.getDeliveryStatus().equalsIgnoreCase("P")){
                TransferInfo transferInfo = transferInfoRepository.findByOrders(orders);
               if(transferInfo == null){
                 return "nopayment";
               }
            for (Items items: orders.getItems()) {
                items.setItemStatus(itemStatusRepository.findByStatus("PC"));
                //items.setDeliveryStatus("PC");
                itemRepository.save(items);
                Products p = productRepository.findOne(items.getProductId());
                DesignerOrderDTO dto= new DesignerOrderDTO();
                dto.setProductName(p.name);
                dto.setStoreName(p.designer.storeName);
                dto.setDesignerEmail(p.designer.user.email);
                dtos.add(dto);
            }

            //update wallet balance
                updateWalletForOrderPayment(customer,transferInfo.getAmountPayed(),"Bank Transfer");
               //done updating wallet balance
                    orders.setDeliveryStatus("PC");
                    orders.setUpdatedOn(date);
                    orders.setUpdatedBy(user.email);
                    orderRepository.save(orders);
                    sendEmailAsync.sendPaymentConfEmailToUser(customer,orders.getOrderNum());
                    sendEmailAsync.sendEmailToDesigner(dtos,orders.getOrderNum());
                }


        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
        return "success";
    }

    private void updateWalletForOrderPayment(User user,Double amount,String paymentType) {

        Wallet w= walletRepository.findByUser(user);

            if (w != null) {
                if(!paymentType.equalsIgnoreCase("Wallet")) {
                    w.setBalance(w.getBalance() + amount);
                }
                w.setPendingSettlement(w.getPendingSettlement() + amount);

            } else {
                w = new Wallet();
                w.setBalance(amount);
                w.setPendingSettlement(amount);
                w.setUser(user);
            }
        System.out.println(w.getPendingSettlement());

        walletRepository.save(w);

    }

    @Override
    public List<Orders> getOrdersByUser(User user) {
        try {
            List<Orders> orders= orderRepository.findByUserId(user.id);
            return orders;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public String addToCart(Cart cart, User user) {
        try{
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
            if(products.priceSlash != null && products.priceSlash.getSlashedPrice()>0){
                amount=products.amount-products.priceSlash.getSlashedPrice();
            }else {
                amount=products.amount;
            }
//
//            int qty = cart.getQuantity();
//
//            Double newAmount = amount*qty;
//            //cartTemp.setAmount(newAmount.toString());

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
    public String updateCart(Cart cart, User user) {
        try{
            Date date = new Date();

            Cart cartTemp = cartRepository.findOne(cart.id);
            double amount;
            Products products = productRepository.findOne(cartTemp.getProductId());
            if(products.priceSlash != null && products.priceSlash.getSlashedPrice()>0){
                amount=products.amount-products.priceSlash.getSlashedPrice();
            }else {
                amount=products.amount;
            }

            int qty = cart.getQuantity();
            cartTemp.setQuantity(cart.getQuantity());
            Double newAmount = amount*qty;
            cartTemp.setAmount(newAmount);
            cartTemp.setUpdatedOn(date);
            cartTemp.setExpiryDate(DateUtils.addDays(date,7));
            cartTemp.setUser(user);
            cartRepository.save(cartTemp);
//
//            String currentAmount = cartTemp.getAmount();
           return "success";

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public String addItemsToCart(CartListDTO carts, User user) {
        try{
            Date date = new Date();

            for (Cart cart: carts.getCarts()) {
                cart.setCreatedOn(date);
                cart.setUpdatedOn(date);
                cart.setExpiryDate(DateUtils.addDays(date,7));
                cart.setUser(user);
                cartRepository.save(cart);
            }

            return cartRepository.countByUser(user).toString();


        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<CartDTO> getCarts(User user) {
        try {
            List<Cart> carts= cartRepository.findByUser(user);
            return generalUtil.convertCartEntsToDTOs(carts);

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
    public void deleteOrder(Long id) {
        try {

            orderRepository.delete(id);

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public void emptyCart(User user) {
        try {


        List<Cart> carts = cartRepository.findByUser(user);
        cartRepository.delete(carts);
//        for (Cart c: carts) {
//            cartRepository.delete(c);
//        }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public List<ItemsRespDTO> getOrdersByDesigner(User user) {
        try {
            ItemStatus itemStatus1 = itemStatusRepository.findByStatus("NV");
            ItemStatus itemStatus2 = itemStatusRepository.findByStatus("C");
            List<ItemStatus> itemStatuses = new ArrayList();
            itemStatuses.add(itemStatus1);
            itemStatuses.add(itemStatus2);
            return generalUtil.convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatusNotIn(user.designer.id,itemStatuses));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public int getSuccessfulSales(User user) {
        try {
            int count = 0;
            if(user.designer !=null) {
                List<Items> items = itemRepository.findByDesignerId(user.designer.id);
                for (Items item : items) {
                    if (item.getOrders().getDeliveryStatus().equalsIgnoreCase("D")) {
                        count = count + 1;
                    }
                }
            }else {
                throw new WawoohException();
            }
           return count;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public List<ItemsRespDTO> getCancelledOrders(User user) {
        try {
            if(user.designer !=null) {
                ItemStatus itemStatus = itemStatusRepository.findByStatus("C");
                return generalUtil.convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatus(user.designer.id,itemStatus));

            }else {
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsRespDTO> getPendingOrders(User user) {
        try {

            if(user.designer !=null) {
                ItemStatus itemStatus = itemStatusRepository.findByStatus("PC");
                return generalUtil.convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatus(user.designer.id,itemStatus));

            }else {
                throw new WawoohException();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsRespDTO> getActiveOrders(User user) {
        try {

            if(user.designer !=null) {
                ItemStatus itemStatus1 = itemStatusRepository.findByStatus("OP");
                ItemStatus itemStatus2 = itemStatusRepository.findByStatus("CO");
                ItemStatus itemStatus3 = itemStatusRepository.findByStatus("RI");
                List<ItemStatus> itemStatuses = new ArrayList();
                itemStatuses.add(itemStatus1);
                itemStatuses.add(itemStatus2);
                itemStatuses.add(itemStatus3);

                return generalUtil.convertItemsEntToDTOs(itemRepository.findActiveOrders(user.designer.id,itemStatuses));


            }else {
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public List<ItemsRespDTO> getCompletedOrders(User user) {
        try {

            if(user.designer !=null) {
                ItemStatus itemStatus = itemStatusRepository.findByStatus("RS");

                return generalUtil.convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatus(user.designer.id,itemStatus));


            }else {
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsRespDTO> getAllOrdersByAdmin(User user) {
        try {

            ItemStatus itemStatus = itemStatusRepository.findByStatus("NV");
            return generalUtil.convertItemsEntToDTOs(itemRepository.findByItemStatusNot(itemStatus));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<OrderDTO> getAllOrdersByAdmin2(User user) {
        try {

            return generalUtil.convertOrderEntsToDTOs(orderRepository.findByDeliveryStatusNot("NV"));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<OrderDTO> getIncompleteOrders(User user) {
        try {

            return generalUtil.convertOrderEntsToDTOs(orderRepository.findByDeliveryStatus("NV"));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsRespDTO> getAllOrdersByQA(User user) {
        try {

                ItemStatus itemStatus1 = itemStatusRepository.findByStatus("RI");
                ItemStatus itemStatus2 = itemStatusRepository.findByStatus("RS");
                ItemStatus itemStatus3 = itemStatusRepository.findByStatus("OS");
                List<ItemStatus> itemStatuses = new ArrayList();
                itemStatuses.add(itemStatus1);
                itemStatuses.add(itemStatus2);
                itemStatuses.add(itemStatus3);

            return generalUtil.convertItemsEntToDTOs(itemRepository.findByItemStatusIn(itemStatuses));

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
    public void saveUserOrderDecision(ItemsDTO itemsDTO,User user) {
        try {

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
//                        items.setItemStatus(itemStatus);
//                        items.setStatusMessage(statusMessage);
 }
itemRepository.save(items);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void saveUserOrderComplain(ItemsDTO itemsDTO, User user) {
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
    public void saveOrderTransferInfo(TransferInfoDTO transferInfoDTO) {
        System.out.println(transferInfoDTO.getOrderNum());
        if(transferInfoDTO.getOrderNum() != null){
            Orders orders = orderRepository.findByOrderNum(transferInfoDTO.getOrderNum());
            if(orders != null){
                if(orders.getDeliveryStatus().equalsIgnoreCase("P")) {
                    TransferInfo transferInfo = transferInfoRepository.findByOrders(orders);
                    if(transferInfo != null){
                        transferInfo.setPaymentDate(transferInfoDTO.getPaymentDate());
                        transferInfo.setAccountName(transferInfoDTO.getAccountName());
                        transferInfo.setAmountPayed(transferInfoDTO.getAmountPayed());
                        transferInfo.setBank(transferInfoDTO.getBank());
                        transferInfo.setPaymentNote(transferInfoDTO.getPaymentNote());
                    }
                    else {
                        transferInfo = new TransferInfo();
                        transferInfo.setOrders(orders);
                        transferInfo.setPaymentDate(transferInfoDTO.getPaymentDate());
                        transferInfo.setAccountName(transferInfoDTO.getAccountName());
                        transferInfo.setAmountPayed(transferInfoDTO.getAmountPayed());
                        transferInfo.setBank(transferInfoDTO.getBank());
                        transferInfo.setPaymentNote(transferInfoDTO.getPaymentNote());
                    }

                    transferInfoRepository.save(transferInfo);
                }
                else {
                    //means admin has updated tp PC. it cant be updated....
                    return;
                }
            }

        }
    }

    @Override
    public TransferInfoDTO getOrderTransferInfo(String orderNum) {

        Orders orders = orderRepository.findByOrderNum(orderNum);
        if(orders != null){
            TransferInfo transferInfo = transferInfoRepository.findByOrders(orders);
            TransferInfoDTO transferInfoDTO = new TransferInfoDTO();
            transferInfoDTO.setPaymentDate(transferInfo.getPaymentDate());
            transferInfoDTO.setAccountName(transferInfo.getAccountName());
            transferInfoDTO.setAmountPayed(transferInfo.getAmountPayed());
            transferInfoDTO.setBank(transferInfo.getBank());
            transferInfoDTO.setPaymentNote(transferInfo.getPaymentNote());

            return transferInfoDTO;
        }
        return null;
    }

    @Override
    public List<TransferInfoDTO> getAllTransferInfo() {

        List<TransferInfo> transferInfos = transferInfoRepository.findAll();
        List<TransferInfoDTO> transferInfoDTOS = new ArrayList<TransferInfoDTO>();
        for (TransferInfo transferInfo : transferInfos) {
            TransferInfoDTO transferInfoDTO = new TransferInfoDTO();
            transferInfoDTO.setId(transferInfo.id);
            transferInfoDTO.setPaymentDate(transferInfo.getPaymentDate());
            transferInfoDTO.setAccountName(transferInfo.getAccountName());
            transferInfoDTO.setAmountPayed(transferInfo.getAmountPayed());
            transferInfoDTO.setBank(transferInfo.getBank());
            transferInfoDTO.setPaymentNote(transferInfo.getPaymentNote());
            transferInfoDTO.setOrderNum(transferInfo.getOrders().getOrderNum());

            transferInfoDTOS.add(transferInfoDTO);
        }

        return transferInfoDTOS;
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



    private void deleteCart(User user){
        List<Cart> carts = cartRepository.findByUser(user);
        for (Cart c: carts) {
            cartRepository.delete(c);
        }
    }


}
