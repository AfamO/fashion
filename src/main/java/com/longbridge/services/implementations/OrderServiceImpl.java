package com.longbridge.services.implementations;

import com.longbridge.dto.CartDTO;
import com.longbridge.dto.CartListDTO;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.OrderDTO;
import com.longbridge.services.OrderService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    CartRepository cartRepository;


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
                p.numOfTimesOrdered = p.numOfTimesOrdered+1;
                p.stockNo=p.stockNo-items.getQuantity();
                productRepository.save(p);

            }

            List<Cart> carts = cartRepository.findByUser(user);
            for (Cart c: carts) {
                cartRepository.delete(c);
            }

            return orderNumber;

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

            String currentAmount = cartTemp.getAmount();
            return currentAmount;

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
    public OrderDTO getOrdersById(Long id) {
        try {

            return convertOrderEntToDTOs(orderRepository.findOne(id));

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

        if(cart.getMaterialPictureId() != null) {
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

        if(cart.getMaterialPictureId() != null) {
            cartDTO.setMeasurementName(measurementRepository.findOne(cart.getMeasurementId()).getName());
        }
        return cartDTO;

    }



    private ItemsDTO convertEntityToDTO(Items items){
        ItemsDTO itemsDTO = new ItemsDTO();

        itemsDTO.setProductId(items.getProductId());
        itemsDTO.setProductName(productRepository.getProductName(items.getProductId()));
        itemsDTO.setAmount(items.getAmount());
        itemsDTO.setColor(items.getColor());
        itemsDTO.setQuantity(items.getQuantity());

        ProductPicture p = productPictureRepository.findFirst1ByProducts(productRepository.findOne(itemsDTO.getProductId()));
        itemsDTO.setProductPicture(p.pictureName);

        if(items.getArtWorkPictureId() != null) {
            ArtWorkPicture a = artWorkPictureRepository.findOne(items.getArtWorkPictureId());
            itemsDTO.setArtWorkPicture(a.pictureName);
        }

        if(items.getMaterialPictureId() != null) {
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
        itemsDTO.setMaterialLocation(items.getMaterialLocation());
        itemsDTO.setMaterialPickupDate(items.getMaterialPickupDate());
        itemsDTO.setMaterialStatus(items.getMaterialStatus());
        itemsDTO.setMaterialPictureId(items.getMaterialPictureId());
        itemsDTO.setDesignerId(items.getDesignerId());
        itemsDTO.setOrderNumber(orders.getOrderNum());
        itemsDTO.setOrderId(orders.id);
        itemsDTO.setMeasurementName(measurementRepository.findOne(items.getMeasurementId()).getName());
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
