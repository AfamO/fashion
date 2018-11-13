package com.longbridge.repository;

import com.longbridge.models.Product;
import com.longbridge.models.ProductColorStyle;
import com.longbridge.models.ProductPicture;
import com.longbridge.models.ProductStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Repository
public interface ProductPictureRepository extends JpaRepository<ProductPicture, Long> {
    List<ProductPicture> findByProductColorStyle_Product(Product product);
    ProductPicture findFirst1ByProductColorStyle_ProductStyle_Product(Product product);
    ProductPicture findFirst1ByProductColorStyle(ProductColorStyle productColorStyle);
}
