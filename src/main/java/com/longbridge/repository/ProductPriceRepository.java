package com.longbridge.repository;

import com.longbridge.models.ProductPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 24/10/2018.
 */
@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long>{


}
