package com.longbridge.repository;

import com.longbridge.models.BespokeProduct;
import com.longbridge.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Longbridge on 22/10/2018.
 */
public interface BespokeProductRepository extends JpaRepository<BespokeProduct, Long> {
    BespokeProduct findByProduct(Product product);
}
