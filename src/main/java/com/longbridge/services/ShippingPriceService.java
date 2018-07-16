package com.longbridge.services;

import com.longbridge.models.User;

public interface ShippingPriceService {

    Object getShippingPrice(Long addresId, User user);
}
