package com.longbridge.repository;


import com.longbridge.models.ItemStatus;

import com.longbridge.models.Product;
import com.longbridge.models.ProductColorStyle;
import com.longbridge.models.ProductSizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 03/07/2018.
 */
@Repository
public interface ProductSizesRepository extends JpaRepository<ProductSizes,Long>{
    @Query("select pr from ProductSizes pr where name  in :itemSizes")
    List<ProductSizes> findByNamesOfSizes(@Param("itemSizes") List<String>  sizes);

    ProductSizes findByProductColorStyleAndName(ProductColorStyle productAttribute, String size);
    List<ProductSizes> findByProductColorStyle(ProductColorStyle productAttribute);
    List<ProductSizes> findByProductColorStyle_ProductStyle_Product(Product product);

}
