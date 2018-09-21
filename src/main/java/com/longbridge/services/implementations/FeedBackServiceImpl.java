package com.longbridge.services.implementations;

import com.longbridge.models.OrderFeedBack;
import com.longbridge.repository.OrderFeedBackRepository;
import com.longbridge.services.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 20/09/2018.
 */
@Service
public class FeedBackServiceImpl implements FeedBackService {
    @Autowired
    OrderFeedBackRepository orderFeedBackRepository;

    @Override
    public void saveOrderFeedBack(OrderFeedBack orderFeedBack) {
        orderFeedBackRepository.save(orderFeedBack);
    }
}
