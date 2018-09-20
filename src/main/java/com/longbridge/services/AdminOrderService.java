package com.longbridge.services;

import com.longbridge.dto.ItemsDTO;
import com.longbridge.dto.OrderReqDTO;
import com.longbridge.respbodydto.ItemsRespDTO;
import com.longbridge.respbodydto.OrderDTO;

import java.util.List;

/**
 * Created by Longbridge on 20/09/2018.
 */
public interface AdminOrderService {
    void updateOrderItemByAdmin(ItemsDTO itemsDTO);
    String updateOrderByAdmin(OrderReqDTO orderReqDTO);
    List<OrderDTO> getAllOrdersByAdmin2();
    List<OrderDTO> getIncompleteOrders();
    List<ItemsRespDTO> getAllOrdersByAdmin();
    void deleteOrder(Long id);
    List<ItemsRespDTO> getAllOrdersByQA();

}
