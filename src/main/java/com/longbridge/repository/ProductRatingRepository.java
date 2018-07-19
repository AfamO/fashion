package com.longbridge.repository;

import com.longbridge.models.ProductRating;
import com.longbridge.models.Products;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Longbridge on 24/04/2018.
 */
public interface ProductRatingRepository extends JpaRepository<ProductRating,Long> {
    List<ProductRating> findByVerifiedFlag(String flag);
    ProductRating findByUserAndProducts(User user, Products products);
    @Query(value = "select pr.products_id, sum(pr.product_quality_rating) as rating FROM product_rating pr GROUP by products_id ORDER BY rating desc Limit 0, 10", nativeQuery = true)
    List<Object[]> findTop10Products();
}
