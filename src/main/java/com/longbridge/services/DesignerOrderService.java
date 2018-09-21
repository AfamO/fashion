package com.longbridge.services;

import com.longbridge.dto.ItemsDTO;
import com.longbridge.respbodydto.ItemsRespDTO;

import java.util.List;

/**
 * Created by Longbridge on 20/09/2018.
 */
public interface DesignerOrderService {

    List<ItemsRespDTO> getOrdersByDesigner();
    void updateOrderItemByDesignerWithMessage(ItemsDTO itemsDTO);
    int getSuccessfulSales();
    List<ItemsRespDTO> getCancelledOrders();
    List<ItemsRespDTO> getPendingOrders();
    List<ItemsRespDTO> getActiveOrders();
    List<ItemsRespDTO> getCompletedOrders();
}
