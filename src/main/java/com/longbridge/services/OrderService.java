package com.longbridge.services;


import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.respbodydto.OrderDTO;

import java.util.List;

/**
 * Created by Longbridge on 03/01/2018.
 */
public interface OrderService {

    String addOrder(OrderReqDTO orders, User user);

    void updateOrderItemByDesignerWithMessage(ItemsDTO itemsDTO, User user);

    List<StatusMessageDTO> updateOrderItemByDesignerr(ItemsDTO itemsDTO, User user);

    void updateOrderItemByAdmin(ItemsDTO itemsDTO, User user);

    void userRejectDecision(ItemsDTO itemsDTO, User user);

    String updateOrderByAdmin(OrderReqDTO orderReqDTO, User user);

    Boolean orderNumExists(String orderNum);

    List<Orders> getOrdersByUser(User user);

    List<ItemsDTO> getOrdersByDesigner(User user);

    int getSuccessfulSales(User user);

    List<ItemsDTO> getCancelledOrders(User user);

    List<ItemsDTO> getPendingOrders(User user);

    List<ItemsDTO> getActiveOrders(User user);

    List<ItemsDTO> getCompletedOrders(User user);

    List<ItemsDTO> getAllOrdersByAdmin(User user);

    List<OrderDTO> getAllOrdersByAdmin2(User user);

    List<ItemsDTO> getAllOrdersByQA(User user);

    String addToCart(Cart cart, User user);

    String updateCart(Cart cart, User user);

    String addItemsToCart(CartListDTO cart, User user);

    void deleteCart(Long id);

    void emptyCart(User user);

    List<CartDTO> getCarts(User user);

    OrderDTO getOrdersById(Long id);

    OrderDTO getOrdersByOrderNum(String orderNumber);

    ItemsDTO getOrderItemById(Long id);

    void saveUserOrderDecision(ItemsDTO itemsDTO,User user);

    void saveUserOrderComplain(ItemsDTO itemsDTO,User user);

    void saveOrderTransferInfo(TransferInfoDTO transferInfoDTO);

    TransferInfoDTO getOrderTransferInfo(String orderNum);

    List<TransferInfoDTO> getAllTransferInfo();
}
