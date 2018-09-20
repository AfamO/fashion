package com.longbridge.services;

import com.longbridge.dto.OrderReqDTO;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 03/08/2018.
 */
public interface WalletService {
        String validateWalletBalance(OrderReqDTO orderReqDTO);
}
