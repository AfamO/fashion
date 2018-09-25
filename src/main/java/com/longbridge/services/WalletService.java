package com.longbridge.services;

import com.longbridge.dto.OrderReqDTO;
import com.longbridge.dto.UserDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 03/08/2018.
 */
public interface WalletService {
        String validateWalletBalance(Double amount);

        String createWallet(UserDTO user);



}
