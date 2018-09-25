package com.longbridge.services;

import com.longbridge.models.Items;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 25/09/2018.
 */
public interface PocketService {

    void updatePocketForOrderPayment(User user, Double amount, String paymentType);

    void updateWalletForOrderDelivery(Items items, User customer);
}
