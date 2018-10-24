package com.longbridge.repository;

import com.longbridge.models.ArtWorkPicture;
import com.longbridge.models.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 14/12/2017.
 */
@Repository
public interface ArtWorkPictureRepository extends JpaRepository<ArtWorkPicture, Long> {
    List<ArtWorkPicture> findByBespokeProduct_Product(Product product);
}
