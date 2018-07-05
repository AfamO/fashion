package com.longbridge.repository;

import com.longbridge.models.ProductSizes;
import com.longbridge.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 03/07/2018.
 */
@Repository
public interface ProductSizesRepository extends JpaRepository<ProductSizes,Long>{
    ProductSizes findByProductsAndName(Products products, String size);
    List<ProductSizes> findByProducts(Products products);
}
