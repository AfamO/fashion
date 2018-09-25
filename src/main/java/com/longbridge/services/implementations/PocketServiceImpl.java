package com.longbridge.services.implementations;

import com.longbridge.models.Items;
import com.longbridge.models.Pocket;
import com.longbridge.models.User;
import com.longbridge.repository.PocketRepository;
import com.longbridge.services.PocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 25/09/2018.
 */
@Service
public class PocketServiceImpl implements PocketService{


    @Autowired
    PocketRepository pocketRepository;
    @Override
    public void updatePocketForOrderPayment(User user, Double amount, String paymentType) {
        Pocket pocket= pocketRepository.findByUser(user);

        if (pocket != null) {
            if(!paymentType.equalsIgnoreCase("Wallet")) {
                pocket.setBalance(pocket.getBalance() + amount);
            }
            pocket.setPendingSettlement(pocket.getPendingSettlement() + amount);

        } else {
            pocket = new Pocket();
            pocket.setBalance(amount);
            pocket.setPendingSettlement(amount);
            pocket.setUser(user);
        }

        pocketRepository.save(pocket);
    }

    @Override
    public void updateWalletForOrderDelivery(Items items, User customer) {
        Pocket pocket = pocketRepository.findByUser(customer);
        pocket.setPendingSettlement(pocket.getPendingSettlement()-items.getAmount());
        pocket.setBalance(pocket.getBalance()-items.getAmount());
        pocketRepository.save(pocket);
    }
}
