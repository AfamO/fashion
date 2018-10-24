package com.longbridge.repository;

import com.longbridge.models.ProductRating;
import com.longbridge.models.Product;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Longbridge on 24/04/2018.
 */
public interface ProductRatingRepository extends JpaRepository<ProductRating,Long> {
    List<ProductRating> findByVerifiedFlag(String flag);
    ProductRating findByUserAndProduct(User user, Product product);
    @Query(value = "select pr.product_id, sum(pr.product_quality_rating) as rating FROM product_rating pr GROUP by product_id ORDER BY rating desc Limit 0, 10", nativeQuery = true)
    List<Object[]> findTop10Products();
}
