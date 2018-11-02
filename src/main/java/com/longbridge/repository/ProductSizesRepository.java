package com.longbridge.repository;

import com.longbridge.models.ItemStatus;
import com.longbridge.models.ProductAttribute;
import com.longbridge.models.ProductSizes;
import com.longbridge.models.Products;
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
    ProductSizes findByProductAttributeAndName(ProductAttribute productAttribute, String size);
    List<ProductSizes> findByProductAttribute(ProductAttribute productAttribute);
    @Query("select pr from ProductSizes pr where name  in :itemSizes")
    List<ProductSizes> findByNamesOfSizes(@Param("itemSizes") List<String>  sizes);
}
