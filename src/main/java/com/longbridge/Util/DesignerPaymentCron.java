package com.longbridge.Util;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.longbridge.models.DesignerPayment;
import com.longbridge.models.Items;
import com.longbridge.models.Pocket;
import com.longbridge.repository.DesignerPaymentRepository;
import com.longbridge.repository.DesignerRepository;
import com.longbridge.repository.ItemRepository;
import com.longbridge.repository.PocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 04/12/2018.
 */
@Component
public class DesignerPaymentCron {

    @Autowired
    PocketRepository pocketRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    DesignerPaymentRepository designerPaymentRepository;

    //@Scheduled(cron = "${wawooh.status.check.rate}")
    private String checkPocketForDebit(){
      //  System.out.println("cron running..........");
        Date date = new Date();
        List<Pocket> pocketList=pocketRepository.findByDueDateForDebitIsLessThanEqualAndDebitFlag(date,"N");
        if(pocketList.size() >0){
    for (Pocket p:pocketList) {
        Items items = itemRepository.findOne(p.getItemId());
        DesignerPayment designerPayment = new DesignerPayment();
        designerPayment.setItems(items);
        designerPayment.setAmount(items.getAmount());
        designerPayment.setPaidFlag("N");
        designerPaymentRepository.save(designerPayment);
    }
}
        return "true";
    }


}
