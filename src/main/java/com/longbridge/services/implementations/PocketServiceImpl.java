package com.longbridge.services.implementations;

import com.longbridge.models.Items;
import com.longbridge.models.Pocket;
import com.longbridge.models.User;
import com.longbridge.repository.PocketRepository;
import com.longbridge.services.PocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Service
public class PocketServiceImpl implements PocketService{


    @Autowired
    PocketRepository pocketRepository;
    @Override
    public void updatePocketForOrderPayment(User user, Double amount, String paymentType,Long itemId) {
       // Pocket pocket= pocketRepository.findByUser(user);
        Pocket pocket= pocketRepository.findByUserAndItemId(user,itemId);
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
            pocket.setItemId(itemId);
        }

        pocketRepository.save(pocket);
    }

    @Override
    public void updateWalletForOrderDelivery(Items items, User customer) {
        Pocket pocket = pocketRepository.findByUserAndItemId(customer,items.id);
        pocket.setPendingSettlement(pocket.getPendingSettlement()-items.getAmount());
        pocket.setBalance(pocket.getBalance()-items.getAmount());
        pocketRepository.save(pocket);
    }


    @Override
    public void setDueDateForPocketDebit(User user, Double amount,Long itemId) {
        Pocket pocket = pocketRepository.findByUserAndItemId(user,itemId);
        pocket.setDueDateForDebit(get7daysFromNowDate());
        pocketRepository.save(pocket);
    }



    private Date get7daysFromNowDate(){
        String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        // Get current date
        Date currentDate = new Date();
        // convert date to localdatetime
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // plus one
        localDateTime = localDateTime.plusDays(7);
        // convert LocalDateTime to date
        Date currentDatePlus7Days = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return currentDatePlus7Days;
    }
}
