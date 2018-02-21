package com.longbridge.repository;

import com.longbridge.models.ArtWorkPicture;
import com.longbridge.models.Products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Repository
public interface ArtWorkPictureRepository extends JpaRepository<ArtWorkPicture, Long> {
    List<ArtWorkPicture> findByProducts(Products products);
}
