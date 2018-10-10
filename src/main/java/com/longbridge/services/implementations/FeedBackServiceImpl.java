package com.longbridge.services.implementations;

import com.longbridge.models.GeneralFeedBack;
import com.longbridge.models.OrderFeedBack;
import com.longbridge.repository.GeneralFeedBackRepository;
import com.longbridge.repository.OrderFeedBackRepository;
import com.longbridge.services.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 20/09/2018.
 */
@Service
public class FeedBackServiceImpl implements FeedBackService {
    @Autowired
    OrderFeedBackRepository orderFeedBackRepository;

    @Autowired
    GeneralFeedBackRepository generalFeedBackRepository;

    @Override
    public void saveOrderFeedBack(OrderFeedBack orderFeedBack) {
        orderFeedBackRepository.save(orderFeedBack);
    }

    @Override
    public List<OrderFeedBack> getAllOrderFeedBacks() {
        return  orderFeedBackRepository.findAll();
    }

    @Override
    public void saveGeneralFeedBack(GeneralFeedBack generalFeedBack) {
        generalFeedBack.setCreatedOn(new Date());
        generalFeedBackRepository.save(generalFeedBack);
    }

    @Override
    public List<GeneralFeedBack> getAllGeneralFeedBacks() {
        return generalFeedBackRepository.findAll();
    }
}
