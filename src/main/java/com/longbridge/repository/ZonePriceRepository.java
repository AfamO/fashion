package com.longbridge.repository;

import com.longbridge.models.ZonePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonePriceRepository extends JpaRepository<ZonePrice, Long> {

    ZonePrice findFirstBySourceAndFromQuantityIsLessThanEqualAndToQuantityIsGreaterThanEqual (String source, int quantityFrom, int quantityTo);
}
