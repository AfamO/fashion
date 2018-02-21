package com.longbridge.repository;

import com.longbridge.models.MaterialPicture;
import com.longbridge.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Repository
public interface MaterialPictureRepository extends JpaRepository<MaterialPicture, Long> {
    List<MaterialPicture> findByProducts(Products products);
}
