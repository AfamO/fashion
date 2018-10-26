package com.longbridge.services;

import com.longbridge.models.PromoCode;
import com.longbridge.models.PromoItem;

import java.util.List;

public interface PromoItemsService {


    void updatePromoItems(PromoCode promoCode);

    void  deletePromoItems(Long id);

    List<PromoItem> ListPromoItems();


}
