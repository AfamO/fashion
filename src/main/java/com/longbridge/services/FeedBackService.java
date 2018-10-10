package com.longbridge.services;

import com.longbridge.models.GeneralFeedBack;
import com.longbridge.models.OrderFeedBack;

import java.util.List;

/**
 * Created by Longbridge on 20/09/2018.
 */
public interface FeedBackService {
    void saveOrderFeedBack(OrderFeedBack orderFeedBack);

    List<OrderFeedBack> getAllOrderFeedBacks();

    void saveGeneralFeedBack(GeneralFeedBack generalFeedBack);
    List<GeneralFeedBack> getAllGeneralFeedBacks();

}
