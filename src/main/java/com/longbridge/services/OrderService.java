package com.longbridge.services;


import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.respbodydto.OrderDTO;
import com.longbridge.respbodydto.OrderRespDTO;

import java.util.List;

/**
 * Created by Longbridge on 03/01/2018.
 */
public interface OrderService {

    PaymentResponse addOrder(OrderReqDTO orders);
    void userRejectDecision(ItemsDTO itemsDTO);
    List<Orders> getOrdersByUser();


    String addToCart(Cart cart);
    String updateCart(Cart cart);
    String addItemsToCart(CartListDTO cart);
    void deleteCart(Long id);
    void emptyCart();
    UserCartDTO getCarts();



    void saveUserOrderDecision(ItemsDTO itemsDTO);
    void saveUserOrderComplain(ItemsDTO itemsDTO);

    List<StatusMessageDTO> updateOrderItemByDesignerr(ItemsDTO itemsDTO, User user);

    String validateItemQuantity(List<Items> items);
    Boolean orderNumExists(String orderNum);
    PaymentResponse cardPayment(Orders orders, String email);
    OrderDTO getOrdersById(Long id);
    OrderDTO getOrdersByOrderNum(String orderNumber);
    ItemsRespDTO getOrderItemById(Long id);
    Boolean thresholdExceeded(Items items);

}
