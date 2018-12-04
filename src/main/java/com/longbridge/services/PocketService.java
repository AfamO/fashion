package com.longbridge.services;

import com.longbridge.models.Items;
import com.longbridge.models.User;


public interface PocketService {

    void updatePocketForOrderPayment(User user, Double amount, String paymentType,Long itemId);

    void updateWalletForOrderDelivery(Items items, User customer);

    void setDueDateForPocketDebit(User user, Double amount,Long itemId);
}
