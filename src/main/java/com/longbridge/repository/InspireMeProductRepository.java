package com.longbridge.repository;

import com.longbridge.models.InspireMe;
import com.longbridge.models.InspireMeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspireMeProductRepository extends JpaRepository<InspireMeProduct, Long> {

    List<InspireMeProduct> findByInspireMeAndProductTypeIdIn(InspireMe inspireMe, List<Long> ids);
}
