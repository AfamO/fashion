package com.longbridge.repository;

import com.longbridge.models.PriceSlash;
import com.longbridge.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 14/05/2018.
 */
@Repository
public interface PriceSlashRepository extends JpaRepository<PriceSlash,Long>{
        PriceSlash findByProducts(Products products);
}
