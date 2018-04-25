package com.longbridge.services;

import com.longbridge.models.ProductRating;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 25/04/2018.
 */
public interface ProductRatingService {
    void RateProduct (User user, ProductRating productRating);
}
