package com.longbridge.services;

import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.PaymentResponse;

public interface AnonymousOrderService {

    PaymentResponse addOrder(OrderReqDTO orders);
}
