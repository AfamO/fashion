package com.longbridge.repository;

import com.longbridge.models.ProductAttribute;
import com.longbridge.models.ProductPicture;
import com.longbridge.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Repository
public interface ProductPictureRepository extends JpaRepository<ProductPicture, Long> {
    List<ProductPicture> findByProducts(Products products);
    ProductPicture findFirst1ByProducts(Products products);
    ProductPicture findFirst1ByProductAttribute(ProductAttribute productAttribute);
}
