package com.longbridge.services;

import com.longbridge.models.Items;
import com.longbridge.models.User;


public interface PocketService {

    void updatePocketForOrderPayment(User user, Double amount, String paymentType);

    void updateWalletForOrderDelivery(Items items, User customer);
}
