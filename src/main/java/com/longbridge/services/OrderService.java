package com.longbridge.services;


import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.respbodydto.DashBoardStatisticsDTO;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.respbodydto.OrderDTO;
import com.longbridge.respbodydto.OrderRespDTO;

import java.util.List;

/**
 * Created by Longbridge on 03/01/2018.
 */
public interface OrderService {

    PaymentResponse addOrder(OrderReqDTO orders, User user);

    void updateOrderItemByDesignerWithMessage(ItemsDTO itemsDTO, User user);

    List<StatusMessageDTO> updateOrderItemByDesignerr(ItemsDTO itemsDTO, User user);

    void updateOrderItemByAdmin(ItemsDTO itemsDTO, User user);

    void userRejectDecision(ItemsDTO itemsDTO, User user);

    String updateOrderByAdmin(OrderReqDTO orderReqDTO, User user);

    Boolean orderNumExists(String orderNum);

    List<Orders> getOrdersByUser(User user);

    List<ItemsRespDTO> getOrdersByDesigner(User user);

    int getSuccessfulSales(User user);

    List<ItemsRespDTO> getCancelledOrders(User user);

    List<ItemsRespDTO> getPendingOrders(User user);

    List<ItemsRespDTO> getActiveOrders(User user);

    List<ItemsRespDTO> getCompletedOrders(User user);

    List<ItemsRespDTO> getAllOrdersByAdmin(User user);
    
    DashBoardStatisticsDTO getDashBoardStatistics();

    List<OrderDTO> getAllOrdersByAdmin2(User user);

    List<OrderDTO> getIncompleteOrders(User user);

    List<ItemsRespDTO> getAllOrdersByQA(User user);

    String addToCart(Cart cart, User user);

    String updateCart(Cart cart, User user);

    String addItemsToCart(CartListDTO cart, User user);

    void deleteCart(Long id);

    void deleteOrder(Long id);

    void emptyCart(User user);

    List<CartDTO> getCarts(User user);

    OrderDTO getOrdersById(Long id);

    OrderDTO getOrdersByOrderNum(String orderNumber);

    ItemsRespDTO getOrderItemById(Long id);

    void saveUserOrderDecision(ItemsDTO itemsDTO,User user);

    void saveUserOrderComplain(ItemsDTO itemsDTO,User user);

    void saveOrderTransferInfo(TransferInfoDTO transferInfoDTO);

    TransferInfoDTO getOrderTransferInfo(String orderNum);

    List<TransferInfoDTO> getAllTransferInfo();
}
