package com.longbridge.repository;

import com.longbridge.models.Product;
import com.longbridge.models.ProductColorStyle;
import com.longbridge.models.ProductStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 22/10/2018.
 */
@Repository
public interface ProductColorStyleRepository extends JpaRepository<ProductColorStyle,Long> {
        List<ProductColorStyle> findByProduct(Product product);
        List<ProductColorStyle> findByProductStyle(ProductStyle productStyle);

}