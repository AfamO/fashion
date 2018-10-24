package com.longbridge.repository;

import com.longbridge.models.Product;
import com.longbridge.models.ProductColorStyle;
import com.longbridge.models.ProductPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Repository
public interface ProductPictureRepository extends JpaRepository<ProductPicture, Long> {
    List<ProductPicture> findByProductStyle_Product(Product product);
    ProductPicture findFirst1ByProductStyle_Product(Product product);
    ProductPicture findFirst1ByProductColorStyle(ProductColorStyle productColorStyle);
}
