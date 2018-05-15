package com.longbridge.services.implementations;

import com.longbridge.Util.SendEmailAsync;
import com.longbridge.dto.*;
import com.longbridge.exception.AppException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
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
            for (Items items: orderReq.getItems()) {
                items.setOrders(orders);
                items.setCreatedOn(date);
                items.setUpdatedOn(date);
                itemRepository.save(items);
                Products p = productRepository.findOne(items.getProductId());
                DesignerOrderDTO dto= new DesignerOrderDTO();
                dto.setProductName(p.name);
                dto.setStoreName(p.designer.storeName);
                dto.setDesignerEmail(p.designer.user.email);
                dtos.add(dto);
                p.numOfTimesOrdered = p.numOfTimesOrdered+1;
                if(p.stockNo != 0){
                    p.stockNo=p.stockNo-items.getQuantity();
                }
                else {
                    p.inStock = "N";
                }
                productRepository.save(p);

            }

            List<Cart> carts = cartRepository.findByUser(user);
            for (Cart c: carts) {
                cartRepository.delete(c);
            }

//            try {
//                Context context = new Context();
//                context.setVariable("name", user.firstName + user.lastName);
//                context.setVariable("orderNum",orderNumber);
//                String message = templateEngine.process("orderemailtemplate", context);
//                mailService.prepareAndSend(message,user.email,messageSource.getMessage("order.success.subject", null, locale));
//
//            }catch (MailException me){
//                me.printStackTrace();
//                throw new AppException(user.firstName + user.lastName,user.email,messageSource.getMessage("order.success.subject", null, locale),orderNumber);
//
//            }

            sendEmailAsync.sendEmailToUser(user,orderNumber);
            sendEmailAsync.sendEmailToDesigner(dtos,orderNumber);
            sendEmailAsync.sendEmailToAdmin(userRepository.findByRole("superadmin"),orderNumber);

            return orderNumber;

        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }


    //todo later
    @Override
    public void updateOrder(Orders orders, User user) {
        try{
            Orders orders1 = orderRepository.findOne(orders.id);
            Date date = new Date();

            orders.setCreatedOn(date);
            orders.setUpdatedOn(date);

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

            return convertItemsEntToDTOs(itemRepository.findByDesignerId(user.designer.id));

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
                    if (item.getOrders().getDeliveryStatus().equalsIgnoreCase("C")) {
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
    public List<ItemsDTO> getAllOrdersByAdmin(User user) {
        try {

            return convertItemsEntToDTOs(itemRepository.findAll());

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
            itemsDTO.setProductName(productRepository.getProductName(items.getProductId()));
            itemsDTO.setAmount(items.getAmount().toString());
            itemsDTO.setColor(items.getColor());
            itemsDTO.setQuantity(items.getQuantity());
            User user=userRepository.findById(items.getOrders().getUserId());
            itemsDTO.setCustomerName(user.lastName+user.firstName);
            itemsDTO.setCustomerId(user.id);
            ProductPicture p = productPictureRepository.findFirst1ByProducts(productRepository.findOne(itemsDTO.getProductId()));
            itemsDTO.setProductPicture(p.pictureName);

            if (items.getArtWorkPictureId() != null) {
                ArtWorkPicture a = artWorkPictureRepository.findOne(items.getArtWorkPictureId());
                itemsDTO.setArtWorkPicture(a.pictureName);
            }

            if (items.getMaterialPictureId() != null) {
                MaterialPicture m = materialPictureRepository.findOne(items.getMaterialPictureId());
                itemsDTO.setMaterialPicture(m.pictureName);
            }
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Orders orders = items.getOrders();
            itemsDTO.setArtWorkPictureId(items.getArtWorkPictureId());
            itemsDTO.setSize(items.getSize());
            //itemsDTO.setDeliveryDate(formatter.format(orders.getDeliveryDate()));
            itemsDTO.setOrderDate(formatter.format(orders.getOrderDate()));
            itemsDTO.setDeliveryStatus(orders.getDeliveryStatus());
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
                itemsDTO.setMeasurement(measurementRepository.findOne(items.getMeasurementId()));
            }
        }
        return itemsDTO;

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
