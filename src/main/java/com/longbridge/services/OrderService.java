package com.longbridge.services;


import com.longbridge.dto.CartDTO;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.Cart;
import com.longbridge.models.Orders;
import com.longbridge.models.User;
import com.longbridge.respbodydto.OrderDTO;

import java.util.List;

/**
 * Created by Longbridge on 03/01/2018.
 */
public interface OrderService {

    String addOrder(OrderReqDTO orders, User user);

    Boolean orderNumExists(String orderNum);

    List<Orders> getOrdersByUser(User user);

    List<ItemsDTO> getOrdersByDesigner(User user);

    String addToCart(Cart cart, User user);

    void deleteCart(Long id);

    void emptyCart(User user);

    List<CartDTO> getCarts(User user);

    OrderDTO getOrdersById(Long id);

}
