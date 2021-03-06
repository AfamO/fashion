package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;

import com.longbridge.Util.ItemsUtil;
import com.longbridge.Util.SendEmailAsync;

import com.longbridge.dto.CloudinaryResponse;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.exception.InvalidAmountException;
import com.longbridge.exception.InvalidStatusUpdateException;
import com.longbridge.exception.PaymentValidationException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.CloudinaryService;
import com.longbridge.services.DesignerOrderService;
import com.longbridge.services.PaymentService;
import com.longbridge.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class DesignerOrderServiceImpl implements DesignerOrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    StatusMessageRepository statusMessageRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;


    @Autowired
    UserRepository userRepository;


    @Autowired
    PaymentService paymentService;

    @Autowired
    WalletService walletService;


    @Autowired
    DesignerRepository designerRepository;


    @Autowired
    ItemRepository itemRepository;


    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    ItemsUtil itemsUtil;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    OrderItemProcessingPictureRepository orderItemProcessingPictureRepository;

    @Autowired
    AnonymousUserRepository anonymousUserRepository;


    @Override
    public List<ItemsRespDTO> getCancelledOrders() {
        try {
            User user= getCurrentUser();
            Designer designer = designerRepository.findByUser(user);
            if(user.getRole().equalsIgnoreCase("designer")) {
                ItemStatus itemStatus = itemStatusRepository.findByStatus("C");
                return generalUtil.convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatus(designer.id,itemStatus));

            }else {
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsRespDTO> getPendingOrders() {
        try {
            User user= getCurrentUser();
            Designer designer = designerRepository.findByUser(user);
            if(user.getRole().equalsIgnoreCase("designer")) {
                ItemStatus itemStatus = itemStatusRepository.findByStatus("P");
                return generalUtil.convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatusOrderByOrders_OrderDateDesc(designer.id,itemStatus));

            }else {
                throw new WawoohException();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsRespDTO> getActiveOrders() {
        try {
            User user= getCurrentUser();
            Designer designer = designerRepository.findByUser(user);
            if(user.getRole().equalsIgnoreCase("designer")) {
                ItemStatus itemStatus1 = itemStatusRepository.findByStatus("OP");
                ItemStatus itemStatus2 = itemStatusRepository.findByStatus("CO");
                ItemStatus itemStatus3 = itemStatusRepository.findByStatus("RI");
                ItemStatus itemStatus4 = itemStatusRepository.findByStatus("A");
                ItemStatus itemStatus5 = itemStatusRepository.findByStatus("PC");
                ItemStatus itemStatus6 = itemStatusRepository.findByStatus("WR");
                List<ItemStatus> itemStatuses = new ArrayList();
                itemStatuses.add(itemStatus1);
                itemStatuses.add(itemStatus2);
                itemStatuses.add(itemStatus3);
                itemStatuses.add(itemStatus4);
                itemStatuses.add(itemStatus5);
                itemStatuses.add(itemStatus6);
                return generalUtil.convertItemsEntToDTOs(itemRepository.findActiveOrders(designer.id,itemStatuses));
            }else {
                throw new WawoohException();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public List<ItemsRespDTO> getCompletedOrders() {
        try {
            User user= getCurrentUser();
            Designer designer = designerRepository.findByUser(user);
            ItemStatus itemStatus = itemStatusRepository.findByStatus("RS");
            return generalUtil.convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatusOrderByOrders_OrderDateDesc(designer.id,itemStatus));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsRespDTO> getOrdersByDesigner() {
        try {
            User user = getCurrentUser();
            ItemStatus itemStatus1 = itemStatusRepository.findByStatus("NV");
            ItemStatus itemStatus2 = itemStatusRepository.findByStatus("C");
            List<ItemStatus> itemStatuses = new ArrayList();
            itemStatuses.add(itemStatus1);
            itemStatuses.add(itemStatus2);
            System.out.println(itemStatuses);
            Designer designer = designerRepository.findByUser(user);
            return generalUtil.convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatusNotInOrderByOrders_OrderDateDesc(designer.id,itemStatuses));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public int getSuccessfulSales() {
        try {
            User user = getCurrentUser();
            int count = 0;
            if(user.getRole().equalsIgnoreCase("designer")) {
                Designer designer = designerRepository.findByUser(user);
                List<Items> items = itemRepository.findByDesignerId(designer.id);
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

    //todo later
    @Override
    public void updateOrderItemByDesignerWithMessage(ItemsDTO itemsDTO) {

            User user=getCurrentUser();
            Date date = new Date();

            User customer;
            if(itemsDTO.getAnonymousFlag() != null && itemsDTO.getAnonymousFlag().equalsIgnoreCase("Y")){
                AnonymousUser anonymousUser = anonymousUserRepository.findOne(itemsDTO.getAnonymousUserId());
                customer = generalUtil.convertAnonymousUsertoTempUser(anonymousUser);
            }else{
                customer = userRepository.findOne(itemsDTO.getCustomerId());
            }

            Items items = itemRepository.findOne(itemsDTO.getId());

            String customerName = customer.getLastName()+" "+ customer.getFirstName();
            Context context = new Context();
            context.setVariable("name", customerName);
            context.setVariable("productName",items.getProductName());

            ItemStatus itemStatus = itemStatusRepository.findOne(itemsDTO.getStatusId());
            if(itemsDTO.getMessageId() != null) {
                StatusMessage statusMessage = statusMessageRepository.findOne(itemsDTO.getMessageId());
            }

            //from payment confirmed to processing
            if(items.getItemStatus().getStatus().equalsIgnoreCase("PC")) {

                if (itemsDTO.getStatus().equalsIgnoreCase("OP")) {
                    items.setItemStatus(itemStatusRepository.findByStatus("OP"));
                }

                else {
                    throw new InvalidStatusUpdateException();
                }
            }
            else if(items.getItemStatus().getStatus().equalsIgnoreCase("WR")){
                if (itemsDTO.getStatus().equalsIgnoreCase("OP")) {
                    items.setItemStatus(itemStatusRepository.findByStatus("OP"));
                }
                else {
                    throw new InvalidStatusUpdateException();
                }
            }


            else if(items.getItemStatus().getStatus().equalsIgnoreCase("P")){
                System.out.println(itemsDTO.getStatus());
                if(itemsDTO.getStatus().equalsIgnoreCase("A")){

                    if(items.getOrders().getPaymentType().equalsIgnoreCase("BANK_TRANSFER")){
                        items.setItemStatus(itemStatus);
                        sendEmailAsync.sendTransferEmailToUser(customer, items.getOrders());
                    }
                    else if(items.getOrders().getPaymentType().equalsIgnoreCase("CARD_PAYMENT")){
                        items.setItemStatus(itemStatus);
                        PaymentResponse p = paymentService.chargeAuthorization(items);
                        //update wallet balance

                        if(p.getStatus().equalsIgnoreCase("99")){
                            //unable to charge customer, cancel transaction
                            items.setItemStatus(itemStatusRepository.findByStatus("P"));
                            itemRepository.save(items);
                            sendEmailAsync.sendOrderPaymentErrorToUser(customer,items.getOrders().getOrderNum(),items.getProductName());
                            throw new PaymentValidationException();
                        }
                    }
                    else if(items.getOrders().getPaymentType().equalsIgnoreCase("WALLET")){
                            // debit user wallet
                            Orders orders = items.getOrders();
                            Double amount = items.getAmount()+orders.getShippingAmount();
                            if(customer.getUserWalletId() == null){
                                items.setItemStatus(itemStatusRepository.findByStatus("P"));
                                itemRepository.save(items);
                                sendEmailAsync.sendOrderPaymentErrorToUser(customer,orders.getOrderNum(),items.getProductName());
                                throw new PaymentValidationException();
                            }
                            String resp = walletService.chargeWallet(amount,orders.getOrderNum(), customer);
                            if(resp.equalsIgnoreCase("00")) {
                                itemsUtil.updateItems(items,amount,items.getOrders().getPaymentType());
                            }else if(resp.equalsIgnoreCase("96")){
                                //send email to the user that he has inssuficient balance or cancel..
                                //currently, we are cancelling
                                items.setItemStatus(itemStatusRepository.findByStatus("P"));
                                itemRepository.save(items);
                                sendEmailAsync.sendOrderPaymentErrorToUser(customer,orders.getOrderNum(),items.getProductName());
                                throw new PaymentValidationException();
                            }
                            else {
                                //unable to charge customer, cancel transaction
                                items.setItemStatus(itemStatusRepository.findByStatus("P"));
                                itemRepository.save(items);
                                sendEmailAsync.sendOrderPaymentErrorToUser(customer,orders.getOrderNum(),items.getProductName());
                                throw new PaymentValidationException();
                            }
                    }
                }
                else if(itemsDTO.getStatus().equalsIgnoreCase("OR")){
                   itemsUtil.updateStockForDesignerDecline(items);
                    sendEmailAsync.sendDeclinedOrderEmailToUser(user,itemsDTO);
                    items.setItemStatus(itemStatusRepository.findByStatus("C"));
                }
                else {
                    throw new InvalidStatusUpdateException();
                }
            }

            //from processing to completed
            else if(items.getItemStatus().getStatus().equalsIgnoreCase("OP")) {
                if (itemsDTO.getStatus().equalsIgnoreCase("CO")) {
                    //get pictures from designer and save
                    savePictures(items,itemsDTO.getPictures());
                    items.setItemStatus(itemStatusRepository.findByStatus("CO"));
                }
                else {
                    throw new InvalidStatusUpdateException();
                }
            }


            items.setUpdatedOn(date);
            itemRepository.save(items);
            Orders orders = orderRepository.findByOrderNum(itemsDTO.getOrderNumber());
            orders.setUpdatedOn(date);
            orders.setUpdatedBy(user.getEmail());
            orderRepository.save(orders);


//        }catch (Exception ex){
//            ex.printStackTrace();
//            throw new WawoohException();
//        }
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }



    private void savePictures(Items items, ArrayList<String> pictures) {
        Date date = new Date();
        List<OrderItemProcessingPicture> orderItemProcessingPictures= orderItemProcessingPictureRepository.findByItems(items);
        for(String p:pictures){
            if(orderItemProcessingPictures.size() < 1) {
                OrderItemProcessingPicture picture = new OrderItemProcessingPicture();
                String pictureName = generalUtil.getPicsName("itempic", items.getProductName().substring(0, 10));
                CloudinaryResponse c = cloudinaryService.uploadToCloud(p, pictureName, "itemprocessingpictures");
                picture.setPictureName(c.getUrl());
                picture.setPicture(c.getPublicId());
                picture.setItems(items);
                picture.createdOn = date;
                picture.setUpdatedOn(date);
                orderItemProcessingPictureRepository.save(picture);
            }else {
                for (OrderItemProcessingPicture o:orderItemProcessingPictures) {
                    cloudinaryService.deleteFromCloud(o.getPicture(), o.getPictureName());
                }
                orderItemProcessingPictureRepository.delete(orderItemProcessingPictures);

                OrderItemProcessingPicture picture = new OrderItemProcessingPicture();
                String pictureName = generalUtil.getPicsName("itempic", items.getProductName().substring(0, 10));
                CloudinaryResponse c = cloudinaryService.uploadToCloud(p, pictureName, "itemprocessingpictures");
                picture.setPictureName(c.getUrl());
                picture.setPicture(c.getPublicId());
                picture.setItems(items);
                picture.createdOn = date;
                picture.setUpdatedOn(date);
                orderItemProcessingPictureRepository.save(picture);
            }
        }
    }


}
