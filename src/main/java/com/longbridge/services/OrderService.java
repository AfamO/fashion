package com.longbridge.services;


import com.longbridge.dto.CartDTO;
import com.longbridge.dto.CartListDTO;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.Cart;
import com.longbridge.models.Orders;
import com.longbridge.models.Products;
import com.longbridge.models.User;
import com.longbridge.respbodydto.OrderDTO;

import java.util.List;

/**
 * Created by Longbridge on 03/01/2018.
 */
public interface OrderService {

    String addOrder(OrderReqDTO orders, User user);

    void updateOrderItemByDesigner(ItemsDTO itemsDTO, User user);

    void updateOrderItemByAdmin(ItemsDTO itemsDTO, User user);

    void updateOrderByAdmin(OrderReqDTO orderReqDTO, User user);

    Boolean orderNumExists(String orderNum);

    List<Orders> getOrdersByUser(User user);

    List<ItemsDTO> getOrdersByDesigner(User user);

    int getSuccessfulSales(User user);

    List<ItemsDTO> getAllOrdersByAdmin(User user);

    List<OrderDTO> getAllOrdersByAdmin2(User user);

    String addToCart(Cart cart, User user);

    String updateCart(Cart cart, User user);

    String addItemsToCart(CartListDTO cart, User user);

    void deleteCart(Long id);

    void emptyCart(User user);

    List<CartDTO> getCarts(User user);

    OrderDTO getOrdersById(Long id);

    ItemsDTO getOrderItemById(Long id);

}
