package com.longbridge.repository;

import com.longbridge.models.ProductRating;
import com.longbridge.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Longbridge on 24/04/2018.
 */
public interface ProductRatingRepository extends JpaRepository<ProductRating,Long> {
}
