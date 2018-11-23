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
import com.longbridge.respbodydto.PromoCodeApplyRespDTO;
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
    ProductColorStyleRepository productColorStyleRepository;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PromoCodeService promoCodeService;

    @Autowired
    PromoCodeRepository promoCodeRepository;

    @Autowired
    PromoCodeUserStatusRepository promoCodeUserStatusRepository;
    
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
                if(items.getProductColorStyleId() != null){
                    ProductColorStyle itemAttribute = productColorStyleRepository.findOne(items.getProductColorStyleId());

                    if(itemAttribute != null){
                        ProductSizes sizes = productSizesRepository.findByProductColorStyleAndName(itemAttribute, items.getSize());
                        if(items.getMeasurementId() == null){
                            if(sizes.getNumberInStock() < items.getQuantity()){
                                orderRespDTO.setStatus("false");
                                return orderRespDTO;
                            }
//                            sizes.setNumberInStock(sizes.getNumberInStock() - items.getQuantity());
//                            productSizes.add(sizes);
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
            if(orderReq.getDeliveryType().equalsIgnoreCase("STANDARD_DELIVERY")) {
                if(orderReq.getDeliveryAddressId() != null) {
                    orders.setDeliveryAddress(addressRepository.findOne(orderReq.getDeliveryAddressId()));
                }else {
                    throw new WawoohException();
                }
            }
            orders.setDeliveryType(orderReq.getDeliveryType());
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
            else {
                System.out.println("Invalid payment type");
                throw new WawoohException();
            }

            orderRepository.save(orders);
            HashMap h= saveItems(orderReq,date,orders,itemStatus);
            totalAmount = Double.parseDouble(h.get("totalAmount").toString());
            totalShippingAmount = Double.parseDouble(h.get("totalShippingAmount").toString());

            orders.setTotalAmount(totalAmount);
            orders.setShippingAmount(totalShippingAmount);

            if(orderReq.getPaymentType().equalsIgnoreCase("CARD_PAYMENT")){
                PaymentRequest paymentRequest = new PaymentRequest();
                paymentRequest.setOrderId(orders.id);
                paymentRequest.setTransactionAmount(totalAmount);
                paymentRequest.setTransactionReference(orderNumber);
                paymentRequest.setEmail(user.getEmail());
                paymentRepository.save(paymentRequest);
                return paymentService.initiatePayment(paymentRequest);
            }


            for (Items items: orderReq.getItems()) {
                if(items.getProductColorStyleId() != null){
                    ProductColorStyle itemAttribute = productColorStyleRepository.findOne(items.getProductColorStyleId());
                    if(itemAttribute != null){
                        ProductSizes sizes = productSizesRepository.findByProductColorStyleAndName(itemAttribute, items.getSize());
                        if(items.getMeasurementId() == null){
                            sizes.setNumberInStock(sizes.getNumberInStock() - items.getQuantity());
                            productSizes.add(sizes);
                        }
                    }
                    }
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

        HashMap hm = new HashMap();
        MaterialPicture material = null;
        ArtWorkPicture artWorkPicture=null;
        Double materialPrice=0.0;
        Double artWorkPrice=0.0;
        Double amount=0.0;
        Double itemsAmount;

        List<String> designerCities = new ArrayList<>();

        for (Items items: orderReq.getItems()) {
            Product p = productRepository.findOne(items.getProductId());
            saveOrderMeasurement(items);

            if(items.getBespokeProductId() != null){

                if(items.getArtWorkPictureId() != null){
                    artWorkPicture=artWorkPictureRepository.findOne(items.getArtWorkPictureId());
                    artWorkPrice=artWorkPicture.getPrice();
                    items.setArtWorkPicture(artWorkPicture.getPictureName());

                }
                if(items.getMaterialPictureId() != null){
                    material = materialPictureRepository.findOne(items.getMaterialPictureId());
                    items.setMaterialPicture(material.getPictureName());
                    materialPrice=material.getPrice();
                    amount= p.getProductPrice().getSewingAmount()+materialPrice+artWorkPrice;
                }
                else {
                    if(items.getMaterialStatus().equalsIgnoreCase("Y")){
                       amount = p.getProductPrice().getSewingAmount()+artWorkPrice;
                    }
                    else {
                        amount = p.getProductPrice().getAmount()+artWorkPrice;
                    }
                }

            }
            else {

                if(p.getProductPrice().getPriceSlash() != null && p.getProductPrice().getPriceSlash().getSlashedPrice() > 0){
                    amount = p.getProductPrice().getPriceSlash().getSlashedPrice();
                }else {
                    amount = p.getProductPrice().getAmount();
                }

            }
            PromoCode promoCode=null;
            //Apply PromoCode Here if the product/item has a promocode
            if(items.getPromoCode()!=null && !items.getPromoCode().equalsIgnoreCase("")){
                promoCode=promoCodeRepository.findUniqueUnExpiredPromoCode(items.getPromoCode());
                PromoCodeUserStatus promoCodeUserStatus= promoCodeUserStatusRepository.findByUserAndPromoCode(getCurrentUser(),promoCode);
                if(promoCodeUserStatus!=null && promoCodeUserStatus.getProductId()==p.id) { // Is the promocode assigned to this product?
                    System.out.println("This item ordered has a promoCode");
                    //Is the value type free shipping?
                    if( !promoCode.getValueType().equalsIgnoreCase("fs")){ // It is either of type percentage discount or normal value discount

                        // Set the new amount to the discounted amount
                        amount = promoCodeUserStatus.getDiscountedAmount();
                    }
                    //Update the promocode user status
                    promoCodeUserStatus.setIsPromoCodeUsedByUser("Y");
                    promoCodeUserStatus.setUpdatedOn(date);
                    promoCodeUserStatusRepository.save(promoCodeUserStatus);
                    // Update the used status if it is a 'single' usage  type or the  number of usage has reached just been reached
                    if(promoCode.getNumberOfUsage()==1 ||(promoCode.getNumberOfUsage()==promoCode.getUsageCounter()+1)){
                        promoCode.setIsUsedStatus("Y");

                    }
                    //Increment the usage counter
                    promoCode.setUsageCounter(promoCode.getUsageCounter()+1);
                    promoCode.setUpdatedOn(new Date());
                    promoCodeRepository.save(promoCode);// Update the PromoCode DB

                }
            }


            itemsAmount = amount*items.getQuantity();
            ProductColorStyle productColorStyle =productColorStyleRepository.findOne(items.getProductColorStyleId());
            items.setProductPicture(productPictureRepository.findFirst1ByProductColorStyle(productColorStyle).getPictureName());

            items.setAmount(itemsAmount);
            if(orderReq.getDeliveryType().equalsIgnoreCase("PICK_UP")){
                totalAmount = totalAmount+itemsAmount;
            }else {
                if(!orders.getDeliveryAddress().getCountry().equalsIgnoreCase("NIGERIA")){
                    totalShippingAmount=shippingUtil.getLocalShipping("NIGERIA",orders.getDeliveryAddress().getCountry(),orderReq.getItems().size());
                }else {
                    if (!designerCities.contains(p.getDesigner().getCity().toUpperCase().trim())) {
                        // Give free shipping for PromoCode of value type 'fs' (Free Shipping)
                        if(promoCode!=null && promoCode.getValueType().equalsIgnoreCase("fs")){
                            shippingAmount=0.0;
                        }
                        else {
                            shippingAmount = shippingUtil.getLocalShipping(p.getDesigner().getCity().toUpperCase().trim(), orders.getDeliveryAddress().getCity().toUpperCase().trim(), items.getQuantity());
                            designerCities.add(p.getDesigner().getCity().toUpperCase().trim());
                        }

                    }
                    totalShippingAmount = totalShippingAmount+shippingAmount;
                }
                totalAmount = totalAmount + itemsAmount + shippingAmount;
            }

            items.setProductSizesId(items.getProductSizesId());
            items.setOrders(orders);
            items.setProductName(p.getName());
            items.setCreatedOn(date);
            items.setUpdatedOn(date);
            items.setItemStatus(itemStatus);
            itemRepository.save(items);
            p.setNumOfTimesOrdered(p.getNumOfTimesOrdered()+1);

            if(items.getBespokeProductId() == null) {
                if(items.getProductSizesId() != null){

                    //todo later, write a reduce stock method
                    ProductSizes productSizes = productSizesRepository.findOne(items.getProductSizesId());
                    if(productSizes != null){
                        if(productSizes.getNumberInStock() != 0){
                            productSizes.setNumberInStock(productSizes.getNumberInStock()-items.getQuantity());
                        }
                    }
                }

            }
            productRepository.save(p);
        }



        hm.put("totalAmount", totalAmount);
        hm.put("totalShippingAmount", totalShippingAmount);
       // hm.put("totalAmountWithoutShipping", totalAmountWithoutShipping);

        return hm;
    }

    private void saveOrderMeasurement(Items items) {
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
            Product product = productRepository.findOne(cart.getProductId());
            Double amount;
            if(product.getProductPrice().getPriceSlash() != null && product.getProductPrice().getPriceSlash().getSlashedPrice()>0){
                amount= product.getProductPrice().getAmount()- product.getProductPrice().getPriceSlash().getSlashedPrice();
            }else {
                amount= product.getProductPrice().getAmount();
            }
            cart.setAmount(amount*cart.getQuantity());
            cart.setProductColorStyleId(cart.getProductColorStyleId());
            cart.setProductSizeId(cart.getProductSizeId());
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
            Product product = productRepository.findOne(cartTemp.getProductId());
            if(product.getProductPrice().getPriceSlash() != null && product.getProductPrice().getPriceSlash().getSlashedPrice()>0){
                amount = product.getProductPrice().getPriceSlash().getSlashedPrice();
            }else {
                amount = product.getProductPrice().getAmount();
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
            if(items.getProductColorStyleId() != null){
                ProductColorStyle itemAttribute = productColorStyleRepository.findOne(items.getProductColorStyleId());

                if(itemAttribute != null){
                    ProductSizes sizes = productSizesRepository.findByProductColorStyleAndName(itemAttribute, items.getSize());
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
