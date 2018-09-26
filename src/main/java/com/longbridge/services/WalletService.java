package com.longbridge.services;

import com.longbridge.dto.OrderReqDTO;
import com.longbridge.dto.UserDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 03/08/2018.
 */
public interface WalletService {
        String validateWalletBalance(OrderReqDTO orderReqDTO);

        String createWallet(UserDTO user, User user1);

        String chargeWallet(Double amount, String orderNum);

        String generateToken(UserDTO user);

        Response getWalletBalance(User user);

}
