package com.longbridge.services;

import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.User;

public interface ShippingPriceService {

    Object getShippingPrice(Long addresId);
    Object getShippingPriceAnonymous(OrderReqDTO orderReqDTO);
}
