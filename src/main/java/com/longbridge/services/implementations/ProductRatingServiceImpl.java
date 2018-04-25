package com.longbridge.services.implementations;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.ProductRating;
import com.longbridge.models.User;
import com.longbridge.repository.ProductRatingRepository;
import com.longbridge.services.ProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 25/04/2018.
 */
@Service
public class ProductRatingServiceImpl implements ProductRatingService {
    @Autowired
    private ProductRatingRepository productRatingRepository;

    @Override
    public void RateProduct(User user, ProductRating productRating) {
        try {
            productRating.setUser(user);
            productRatingRepository.save(productRating);
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

}
