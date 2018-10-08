package com.longbridge.services.implementations;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SMSAlertUtil;
import com.longbridge.Util.SendEmailAsync;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.exception.AppException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.respbodydto.OrderDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.AdminOrderService;
import com.longbridge.services.MailService;
import com.longbridge.services.PocketService;
import com.longbridge.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.*;


@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MailService mailService;


    @Autowired
    PocketService pocketService;

    @Autowired
    TransferInfoRepository transferInfoRepository;



    private Locale locale = LocaleContextHolder.getLocale();


    @Autowired
    ItemRepository itemRepository;


    @Autowired
    SMSAlertUtil smsAlertUtil;


    @Autowired
    GeneralUtil generalUtil;



    @Override
    public void updateOrderItemByAdmin(ItemsDTO itemsDTO) {

        try{
            ItemsDTO itemsDTO1 = new ItemsDTO();
            Items items = itemRepository.findOne(itemsDTO.getId());
            Orders orders=orderRepository.findOne(items.getOrders().id);
            Date date = new Date();
            items.setUpdatedOn(date);
            User customer = userRepository.findOne(itemsDTO.getCustomerId());
            String customerEmail = customer.getEmail();
            String customerName = customer.getLastName()+" "+ customer.getFirstName();
            Context context = new Context();
            context.setVariable("name", customerName);
            context.setVariable("productName",items.getProductName());

            try {
                ItemStatus itemStatus = itemStatusRepository.findOne(itemsDTO.getStatusId());
                if(items.getItemStatus().getStatus().equalsIgnoreCase("CO")) {
                    if (itemsDTO.getStatus().equalsIgnoreCase("RI")) {
                        items.setItemStatus(itemStatus);
                        //notify designer to bring items to wawooh office
                        String message = templateEngine.process("readyforinsptemplate", context);
                        mailService.prepareAndSend(message, customerEmail, messageSource.getMessage("order.inspection.subject", null, locale));
                    }

                }

                if(items.getItemStatus().getStatus().equalsIgnoreCase("RI")) {
                    if (itemsDTO.getStatus().equalsIgnoreCase("WC")) {
                        items.setItemStatus(itemStatusRepository.findByStatus("WC"));
                    }
                }


                if(items.getItemStatus().getStatus().equalsIgnoreCase("WC")){
                    if(itemsDTO.getStatus().equalsIgnoreCase("PI")){
                        if(items.getFailedInspectionReason() != null){
                            items.setFailedInspectionReason(null);
                        }
                        items.setItemStatus(itemStatusRepository.findByStatus("RS"));
                    }
                    else if(itemsDTO.getStatus().equalsIgnoreCase("FI")){
                        items.setItemStatus(itemStatusRepository.findByStatus("WR"));
                        items.setFailedInspectionReason(itemsDTO.getAction());
                        //todo later, send email to user and designer
                        itemsDTO.setProductName(items.getProductName());
                        sendEmailAsync.sendFailedInspEmailToUser(customer,itemsDTO);
                        sendEmailAsync.sendFailedInspToDesigner(itemsDTO);
                    }

                }
                else if(items.getItemStatus().getStatus().equalsIgnoreCase("WR")){
                    if(itemsDTO.getStatus().equalsIgnoreCase("DP")){
                        items.setItemStatus(itemStatusRepository.findByStatus("RI"));
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
                        pocketService.updateWalletForOrderDelivery(items, customer);
                        //end updating wallet balance
                        String encryptedMail = Base64.getEncoder().encodeToString(customerEmail.getBytes());
                        String link = messageSource.getMessage("order.complain",null, locale);
                        String feedBackLink = messageSource.getMessage("order.feedback",null, locale);
                        link = link+encryptedMail+"&itemId="+items.id+"&orderNum="+itemsDTO.getOrderNumber();
                        feedBackLink=feedBackLink+encryptedMail+"&itemId="+items.id+"&orderNum="+itemsDTO.getOrderNumber();
                        context.setVariable("link",link);
                        context.setVariable("feedbacklink",feedBackLink);
                        String message = templateEngine.process("oderdeliveredemail", context);
                        mailService.prepareAndSend(message,customerEmail,messageSource.getMessage("order.delivered.subject", null, locale));
                    }
                }

                //Orders orders = orderRepository.findByOrderNum(itemsDTO.getOrderNumber());
                items.setUpdatedOn(date);
                itemRepository.save(items);
                orders.setUpdatedOn(date);
                orders.setUpdatedBy(getCurrentUser().getEmail());
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

    @Override
    public void deleteOrder(Long id) {
        try {
            Orders orders = orderRepository.findOne(id);
            List<Items> items = itemRepository.findByOrders(orders);
            itemRepository.delete(items);
            orderRepository.delete(orders);

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public String updateOrderByAdmin(OrderReqDTO orderReqDTO) {
        try{

            User customer = userRepository.findOne(orderReqDTO.getUserId());
            Orders orders=orderRepository.findOne(orderReqDTO.getId());
            Date date = new Date();
            orders.setUpdatedOn(date);
//            if(orders.getDeliveryStatus().equalsIgnoreCase("A")){
            TransferInfo transferInfo = transferInfoRepository.findByOrders(orders);
            if(transferInfo == null){
                return "nopayment";
            }
            if(transferInfo.getAmountPayed() < orders.getTotalAmount()){
                return "lesspayment";
            }
            for (Items items: orders.getItems()) {
                if (items.getMeasurement() != null) {
                    items.setItemStatus(itemStatusRepository.findByStatus("PC"));
                }
                else {
                    items.setItemStatus(itemStatusRepository.findByStatus("RI"));
                }
                itemRepository.save(items);
                notifyDesigner(items);

                if (items.getMeasurement() != null) {
                    items.setItemStatus(itemStatusRepository.findByStatus("PC"));
                }
                else {
                    items.setItemStatus(itemStatusRepository.findByStatus("RI"));
                }
            }
            //update wallet balance
            pocketService.updatePocketForOrderPayment(customer,transferInfo.getAmountPayed(),"BANK_TRANSFER");
            //update orders

            orders.setDeliveryStatus("PC");
            orders.setUpdatedOn(date);
            orders.setUpdatedBy(getCurrentUser().getEmail());
            orderRepository.save(orders);
            sendEmailAsync.sendPaymentConfEmailToUser(customer,orders.getOrderNum());
//                }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
        return "success";
    }


    @Override
    public List<ItemsRespDTO> getAllOrdersByAdmin() {
        try {

            ItemStatus itemStatus = itemStatusRepository.findByStatus("NV");
            return generalUtil.convertItemsEntToDTOs(itemRepository.findByItemStatusNotOrderByOrders_OrderDateDesc(itemStatus));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }




    @Override
    public List<OrderDTO> getAllOrdersByAdmin2() {
        try {

            return generalUtil.convertOrderEntsToDTOs(orderRepository.findByDeliveryStatusNotOrderByOrderDateDesc("NV"));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<OrderDTO> getIncompleteOrders() {
        try {
            return generalUtil.convertOrderEntsToDTOs(orderRepository.findByDeliveryStatusOrderByOrderDateDesc("NV"));
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsRespDTO> getAllOrdersByQA() {
        try {
//
//                ItemStatus itemStatus1 = itemStatusRepository.findByStatus("RI");
//                ItemStatus itemStatus2 = itemStatusRepository.findByStatus("RS");
//                ItemStatus itemStatus3 = itemStatusRepository.findByStatus("OS");
//                ItemStatus itemStatus4 = itemStatusRepository.findByStatus("CO");
//                ItemStatus itemStatus5 = itemStatusRepository.findByStatus("WC");
//                ItemStatus itemStatus6 = itemStatusRepository.findByStatus("WR");
//                List<ItemStatus> itemStatuses = new ArrayList();
//                itemStatuses.add(itemStatus1);
//                itemStatuses.add(itemStatus2);
//                itemStatuses.add(itemStatus3);
//                itemStatuses.add(itemStatus4);
//                itemStatuses.add(itemStatus5);
//                itemStatuses.add(itemStatus6);

            //   return generalUtil.convertItemsEntToDTOs(itemRepository.findByItemStatusInOrderByOrders_OrderDateDesc(itemStatuses));
            return generalUtil.convertItemsEntToDTOs(itemRepository.findByItemStatus_StatusInOrderByOrders_OrderDateDesc(Arrays.asList("RI","RS","OS","CO","WC","WR")));


        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }

    private void notifyDesigner(Items items) throws IOException {
        Products p = productRepository.findOne(items.getProductId());
        String storeName = p.getDesigner().getStoreName();
        storeName=storeName.replaceAll(" ","");
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(p.getDesigner().getUser().getPhoneNo());
        String link = "http://fashion-wawooh.herokuapp.com/";
        link = link +storeName+"/orders/" + items.id;
        String message = null;
        if(items.getMeasurement() != null){
            message = String.format(messageSource.getMessage("order.designer.startprocessing", null, locale), link);
        }
        else {
            message = String.format(messageSource.getMessage("order.designer.sendtoqa", null, locale), link);
        }
        smsAlertUtil.sms(phoneNumbers,message);
    }

//    private void updateWalletForOrderPayment(User user,Double amount,String paymentType) {
//
//        Wallet w= pocketRepository.findByUser(user);
//        if (w != null) {
//            if(!paymentType.equalsIgnoreCase("Wallet")) {
//                w.setBalance(w.getBalance() + amount);
//            }
//            w.setPendingSettlement(w.getPendingSettlement() + amount);
//        } else {
//            w = new Wallet();
//            w.setBalance(amount);
//            w.setPendingSettlement(amount);
//            w.setUser(user);
//        }
//        System.out.println(w.getPendingSettlement());
//        pocketRepository.save(w);
//    }
//


    @Override
    public void updateTrackingNumber(ItemsDTO itemsDTO) {
        try {
            Items items = itemRepository.findOne(itemsDTO.getId());

            if(items != null){
                items.setTrackingNumber(itemsDTO.getTrackingNumber());
                itemRepository.save(items);
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


}
