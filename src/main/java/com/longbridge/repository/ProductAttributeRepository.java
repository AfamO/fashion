package com.longbridge.repository;

import com.longbridge.models.ProductAttribute;
import com.longbridge.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 15/08/2018.
 */
@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute,Long> {
    List<ProductAttribute> findByProducts(Products products);
}
