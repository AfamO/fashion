package com.longbridge.services;

import com.longbridge.models.PriceSlash;

/**
 * Created by Longbridge on 14/05/2018.
 */
public interface PriceSlashService {
     void addPriceSlash(PriceSlash priceslash);
     void removePriceSlash(Long productId);
}
