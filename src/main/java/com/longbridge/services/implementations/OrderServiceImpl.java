package com.longbridge.services.implementations;

import com.longbridge.Util.SendEmailAsync;
import com.longbridge.dto.*;
import com.longbridge.exception.AppException;
import com.longbridge.exception.InvalidStatusUpdateException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.OrderDTO;
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
    private TemplateEngine templateEngine;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MailService mailService;

    private Locale locale = LocaleContextHolder.getLocale();


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AddressRepository addressRepository;

//
//    @Value("${product.picture.folder}")
//    private String productPicturesFolder;
//
//    @Value("${artwork.picture.folder}")
//    private String artWorkPictureFolder;
//
//    @Value("${material.picture.folder}")
//    private String materialPictureFolder;

    @Override
    public String addOrder(OrderReqDTO orderReq, User user) {

        try{
            List<DesignerOrderDTO> dtos = new ArrayList<>();
            Orders orders = new Orders();
            Date date = new Date();

            orders.setCreatedOn(date);
            orders.setUpdatedOn(date);
            orders.setUserId(user.id);
            orders.setDeliveryStatus("P");
            orders.setOrderDate(date);
            orders.setPaymentType(orderReq.getPaymentType());
            orders.setTotalAmount(orderReq.getTotalAmount());

            orders.setDeliveryAddress(addressRepository.findOne(orderReq.getDeliveryAddressId()));
            String orderNumber = "";

            while (!orderNumExists(generateOrderNum())){
                orderNumber = "WAW#"+generateOrderNum();
                orders.setOrderNum(orderNumber);
                break;
            }
            orderRepository.save(orders);
            ItemStatus itemStatus = itemStatusRepository.findByStatus("P");
            for (Items items: orderReq.getItems()) {
                Products p = productRepository.findOne(items.getProductId());
                if(items.getMeasurementId() != null) {
                    Measurement measurement = measurementRepository.findOne(items.getMeasurementId());
                    items.setMeasurement(measurement.toString());
                }
                if(items.getArtWorkPictureId() != null){
                    items.setArtWorkPicture(artWorkPictureRepository.findOne(items.getArtWorkPictureId()).pictureName);
                }
                if(items.getMaterialPictureId() != null){
                    items.setMaterialPicture(materialPictureRepository.findOne(items.getMaterialPictureId()).pictureName);
                }

                items.setProductPicture(productPictureRepository.findFirst1ByProducts(p).pictureName);

                items.setOrders(orders);
                items.setProductName(p.name);
                items.setCreatedOn(date);
                items.setUpdatedOn(date);
                items.setItemStatus(itemStatus);
                itemRepository.save(items);


                p.numOfTimesOrdered = p.numOfTimesOrdered+1;
                if(p.stockNo != 0){
                    p.stockNo=p.stockNo-items.getQuantity();

                }
                else {
                    p.inStock = "N";
                }
                productRepository.save(p);
                if(p.stockNo==0){
                    p.inStock = "N";
                }

            }

            List<Cart> carts = cartRepository.findByUser(user);
            for (Cart c: carts) {
                cartRepository.delete(c);
            }


            sendEmailAsync.sendEmailToUser(user,orderNumber);
           //sendEmailAsync.sendEmailToDesigner(dtos,orderNumber);
//            sendEmailAsync.sendEmailToAdmin(userRepository.findByRole("superadmin"),orderNumber);

            return orderNumber;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    //todo later
    @Override
    public void updateOrderItemByDesignerWithMessage(ItemsDTO itemsDTO, User user) {
        try{
            ItemsDTO itemsDTO1 = new ItemsDTO();
            User customer = userRepository.findOne(itemsDTO.getCustomerId());
            String customerEmail = customer.email;
            String rejectDecisionLink = "";
            String customerName = customer.lastName+" "+ customer.firstName;
            Context context = new Context();
            context.setVariable("name", customerName);

            Items items = itemRepository.findOne(itemsDTO.getId());
            Products products = productRepository.findOne(items.getProductId());
            context.setVariable("productName",products.name);
            Date date = new Date();
            System.out.println(items.getDeliveryStatus());
            System.out.println(itemsDTO.getDeliveryStatus());

            ItemStatus itemStatus = itemStatusRepository.findOne(itemsDTO.getStatusId());

            StatusMessage statusMessage = statusMessageRepository.findOne(itemsDTO.getMessageId());


            if(items.getItemStatus().getStatus().equalsIgnoreCase("PC")) {
                if(itemsDTO.getStatus().equalsIgnoreCase("OP")){

                    items.setItemStatus(itemStatus);
                    //items.setStatusMessage(statusMessage);
                }
                else if(itemsDTO.getStatus().equalsIgnoreCase("OR")){
                    String encryptedMail = Base64.getEncoder().encodeToString(customerEmail.getBytes());
                    String link=messageSource.getMessage("order.reject.decision", null, locale);
                    rejectDecisionLink=link+encryptedMail+"&itemId="+items.id+"&orderNum="+itemsDTO.getOrderNumber();
                    statusMessage.setHasResponse(true);
                    context.setVariable("link",rejectDecisionLink);
                    context.setVariable("waitTime",itemsDTO.getWaitTime());
                    items.setItemStatus(itemStatus);
                    items.setStatusMessage(statusMessage);
                    String message = templateEngine.process("admincancelordertemplate", context);

                    itemsDTO1.setDeliveryStatus(items.getItemStatus().getStatus());
                    itemsDTO1.setProductName(products.name);
                    itemsDTO1.setLink(rejectDecisionLink);
                    itemsDTO1.setWaitTime(itemsDTO.getWaitTime());
                    System.out.println("i got hwere1");
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
                if(itemsDTO.getStatus().equalsIgnoreCase("CO")){
                    items.setItemStatus(itemStatus);
                    statusMessage.setHasResponse(false);
                    items.setStatusMessage(statusMessage);
                }
                else {
                    throw new InvalidStatusUpdateException();
                }
            }

            System.out.println("i got hwere2");
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
            context.setVariable("productName",itemsDTO.getProductName());
            try {
                ItemStatus itemStatus = itemStatusRepository.findOne(itemsDTO.getStatusId());

              //  StatusMessage statusMessage = statusMessageRepository.findOne(itemsDTO.getMessageId());

            if(items.getItemStatus().getStatus().equalsIgnoreCase("CO")){
                if(itemsDTO.getStatus().equalsIgnoreCase("RI")){
                    items.setItemStatus(itemStatus);
                    //items.setStatusMessage(statusMessage);

                    String message = templateEngine.process("readyforinsptemplate", context);
                    mailService.prepareAndSend(message,customerEmail,messageSource.getMessage("order.inspection.subject", null, locale));

                }
            }
            else if(items.getItemStatus().getStatus().equalsIgnoreCase("RI")){
                if(itemsDTO.getStatus().equalsIgnoreCase("RS")){
                    items.setItemStatus(itemStatus);
                    //items.setStatusMessage(statusMessage);

                    String message = templateEngine.process("ordershippedemail", context);
                    mailService.prepareAndSend(message,customerEmail,messageSource.getMessage("order.shipped.subject", null, locale));

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
                    //items.setStatusMessage(statusMessage);

                    String message = templateEngine.process("orderdeliveredemail", context);
                    mailService.prepareAndSend(message,customerEmail,messageSource.getMessage("order.delivered.subject", null, locale));

                }
            }
                //Orders orders = orderRepository.findByOrderNum(itemsDTO.getOrderNumber());
                orders.setUpdatedOn(date);
                orders.setUpdatedBy(user.email);
                orderRepository.save(orders);

                itemsDTO1.setDeliveryStatus(items.getItemStatus().getStatus());
                itemsDTO1.setProductName(itemsDTO.getProductName());


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
    public void userRejectDecision(ItemsDTO itemsDTO, User user) {
        try{
            Items items = itemRepository.findOne(itemsDTO.getId());
            if(itemsDTO.getAction().equalsIgnoreCase("accept")) {
                items.setItemStatus(itemStatusRepository.findByStatus("PC"));
            }
            else if(itemsDTO.getAction().equalsIgnoreCase("refund")){
                Refund refund = new Refund();
                refund.setAccountName(itemsDTO.getAccountName());
                refund.setAccountNumber(itemsDTO.getAccountNumber());
                items.setItemStatus(itemStatusRepository.findByStatus("C"));

            }
            itemRepository.save(items);
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void updateOrderByAdmin(OrderReqDTO orderReqDTO, User user) {
        try{
            List<DesignerOrderDTO> dtos = new ArrayList<>();
            User customer = userRepository.findOne(orderReqDTO.getUserId());

            Orders orders=orderRepository.findOne(orderReqDTO.getId());
            Date date = new Date();
            orders.setUpdatedOn(date);

            if(orders.getDeliveryStatus().equalsIgnoreCase("P")){
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
                    customer.walletBalance=orderReqDTO.getPaidAmount();
                    userRepository.save(customer);
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
            cart.setAmount(Double.toString(products.amount*cart.getQuantity()));
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
            double amount =productRepository.findOne(cartTemp.getProductId()).amount;
            int qty = cart.getQuantity();
            cartTemp.setQuantity(cart.getQuantity());
            Double newAmount = amount*qty;
            cartTemp.setAmount(newAmount.toString());
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
            return convertCartEntsToDTOs(carts);

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
    public List<ItemsDTO> getOrdersByDesigner(User user) {
        try {
            ItemStatus itemStatus = itemStatusRepository.findByStatus("C");
            return convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatusNot(user.designer.id,itemStatus));

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
    public List<ItemsDTO> getCancelledOrders(User user) {
        try {
            if(user.designer !=null) {
                ItemStatus itemStatus = itemStatusRepository.findByStatus("C");
                return convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatus(user.designer.id,itemStatus));

            }else {
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsDTO> getPendingOrders(User user) {
        try {

            if(user.designer !=null) {
                ItemStatus itemStatus = itemStatusRepository.findByStatus("PC");
                return convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatus(user.designer.id,itemStatus));

            }else {
                throw new WawoohException();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsDTO> getActiveOrders(User user) {
        try {

            if(user.designer !=null) {
                ItemStatus itemStatus1 = itemStatusRepository.findByStatus("OP");
                ItemStatus itemStatus2 = itemStatusRepository.findByStatus("CO");
                ItemStatus itemStatus3 = itemStatusRepository.findByStatus("RI");
                List<ItemStatus> itemStatuses = new ArrayList();
                itemStatuses.add(itemStatus1);
                itemStatuses.add(itemStatus2);
                itemStatuses.add(itemStatus3);

                return convertItemsEntToDTOs(itemRepository.findActiveOrders(user.designer.id,itemStatuses));


            }else {
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public List<ItemsDTO> getCompletedOrders(User user) {
        try {

            if(user.designer !=null) {
                ItemStatus itemStatus = itemStatusRepository.findByStatus("RS");

                return convertItemsEntToDTOs(itemRepository.findByDesignerIdAndItemStatus(user.designer.id,itemStatus));


            }else {
                throw new WawoohException();
            }

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<ItemsDTO> getAllOrdersByAdmin(User user) {
        try {

            return convertItemsEntToDTOs(itemRepository.findAll());

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<OrderDTO> getAllOrdersByAdmin2(User user) {
        try {

            return convertOrderEntsToDTOs(orderRepository.findAll());

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public OrderDTO getOrdersById(Long id) {
        try {

            return convertOrderEntToDTOs(orderRepository.findOne(id));

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public ItemsDTO getOrderItemById(Long id) {
        try {

            return convertEntityToDTO(itemRepository.findOne(id));

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





    private List<ItemsDTO> convertItemsEntToDTOs(List<Items> items){

        List<ItemsDTO> itemsDTOS = new ArrayList<ItemsDTO>();

        for(Items items1: items){
            ItemsDTO itemsDTO = convertEntityToDTO(items1);
            itemsDTOS.add(itemsDTO);
        }
        return itemsDTOS;
    }

    private List<CartDTO> convertCartEntsToDTOs(List<Cart> carts){
        List<CartDTO> cartDTOS = new ArrayList<>();
        for(Cart cart:carts){
            CartDTO cartDTO = convertCartEntToDTO(cart);
            cartDTOS.add(cartDTO);
        }
        return cartDTOS;
    }

    private CartDTO convertCartEntToDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();

        cartDTO.setId(cart.id);

        cartDTO.setProductId(cart.getProductId());

        Products products = productRepository.findOne(cart.getProductId());

        cartDTO.setProductName(products.name);

        ProductPicture p = productPictureRepository.findFirst1ByProducts(products);
        cartDTO.setProductPicture(p.pictureName);
        cartDTO.setStockNo(products.stockNo);

        if(cart.getArtWorkPictureId() != null) {
            ArtWorkPicture a = artWorkPictureRepository.findOne(cart.getArtWorkPictureId());
            cartDTO.setArtWorkPicture(a.pictureName);
            cartDTO.setArtWorkPictureId(cart.getArtWorkPictureId());
        }

        System.out.println(cart.getMaterialPictureId());
        if(cart.getMaterialPictureId() != null) {
            System.out.println(cart.getMaterialPictureId());
            MaterialPicture m = materialPictureRepository.findOne(cart.getMaterialPictureId());
            cartDTO.setMaterialPicture(m.pictureName);
            cartDTO.setMaterialPictureId(cart.getMaterialPictureId());
        }

        cartDTO.setAmount(cart.getAmount());
        cartDTO.setColor(cart.getColor());
        cartDTO.setQuantity(cart.getQuantity());
        cartDTO.setSize(cart.getSize());
        cartDTO.setMaterialLocation(cart.getMaterialLocation());
        cartDTO.setMaterialPickupDate(cart.getMaterialPickupDate());
        cartDTO.setMaterialStatus(cart.getMaterialStatus());
        cartDTO.setDesignerId(cart.getDesignerId());

        if(cart.getMeasurementId() != null) {
            Measurement m = measurementRepository.findOne(cart.getMeasurementId());
            cartDTO.setMeasurementName(m.getName());
            cartDTO.setMeasurementId(cart.getMeasurementId());
        }
        return cartDTO;

    }



    private ItemsDTO convertEntityToDTO(Items items){
        ItemsDTO itemsDTO = new ItemsDTO();
        if(items != null) {
            itemsDTO.setId(items.id);
            itemsDTO.setProductId(items.getProductId());
            Products p = productRepository.findOne(items.getProductId());
            itemsDTO.setProductName(p.name);
            itemsDTO.setProductAvailability(p.availability);
            itemsDTO.setAmount(items.getAmount().toString());
            itemsDTO.setColor(items.getColor());
            itemsDTO.setQuantity(items.getQuantity());
            User user=userRepository.findById(items.getOrders().getUserId());
            itemsDTO.setCustomerName(user.lastName+" "+user.firstName);
            itemsDTO.setCustomerId(user.id);
           // ProductPicture p = productPictureRepository.findFirst1ByProducts(productRepository.findOne(itemsDTO.getProductId()));
            itemsDTO.setProductPicture(items.getProductPicture());

           // if (items.getArtWorkPictureId() != null) {
              //  ArtWorkPicture a = artWorkPictureRepository.findOne(items.getArtWorkPictureId());
                itemsDTO.setArtWorkPicture(items.getArtWorkPicture());
            //}

           // if (items.getMaterialPictureId() != null) {
              //  MaterialPicture m = materialPictureRepository.findOne(items.getMaterialPictureId());
                itemsDTO.setMaterialPicture(items.getMaterialPicture());
            //}
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Orders orders = items.getOrders();
            itemsDTO.setArtWorkPictureId(items.getArtWorkPictureId());
            itemsDTO.setSize(items.getSize());
            //itemsDTO.setDeliveryDate(formatter.format(orders.getDeliveryDate()));
            itemsDTO.setOrderDate(formatter.format(orders.getOrderDate()));
            //itemsDTO.setDeliveryStatus(items.getDeliveryStatus());
            itemsDTO.setStatus(items.getItemStatus().getStatus());
            itemsDTO.setStatusId(items.getItemStatus().id);
            if(items.getMaterialLocation() != null){
                itemsDTO.setMaterialLocation(items.getMaterialLocation().toString());
            }
            itemsDTO.setMaterialPickupDate(items.getMaterialPickupDate());
            itemsDTO.setMaterialStatus(items.getMaterialStatus());
            itemsDTO.setMaterialPictureId(items.getMaterialPictureId());
            itemsDTO.setDesignerId(items.getDesignerId());
            itemsDTO.setOrderNumber(orders.getOrderNum());
            itemsDTO.setOrderId(orders.id);
            if (items.getMeasurementId() != null) {
                itemsDTO.setMeasurement(items.getMeasurement());
            }
        }
        return itemsDTO;

    }


    private List<OrderDTO> convertOrderEntsToDTOs(List<Orders> orders){
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for(Orders orders1:orders){
            OrderDTO orderDTO = convertOrderEntToDTOs(orders1);
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    private OrderDTO convertOrderEntToDTOs(Orders orders){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orders.id);
        orderDTO.setDeliveryAddress(orders.getDeliveryAddress().getAddress());
        orderDTO.setDeliveryStatus(orders.getDeliveryStatus());
        orderDTO.setOrderNumber(orders.getOrderNum());
        orderDTO.setPaymentType(orders.getPaymentType());
        orderDTO.setTotalAmount(orders.getTotalAmount());
        orderDTO.setUserId(orders.getUserId());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(orders.getDeliveryDate() != null) {
            orderDTO.setDeliveryDate(formatter.format(orders.getDeliveryDate()));
        }

        orderDTO.setOrderDate(formatter.format(orders.getOrderDate()));
        orderDTO.setItemsList(convertItemsEntToDTOs(orders.getItems()));

        return orderDTO;

    }


}
