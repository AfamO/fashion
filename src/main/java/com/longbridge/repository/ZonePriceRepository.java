package com.longbridge.repository;

import com.longbridge.models.ZonePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonePriceRepository extends JpaRepository<ZonePrice, Long> {

    //ZonePrice findFirstBySourceAndFromQuantityIsLessThanEqualAndToQuantityIsGreaterThanEqual (String source, int quantityFrom, int quantityTo);

    @Query("select z.zoneOnePrice from ZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneOnePrice(@Param("quantity") int quantity);

    @Query("select z.zoneTwoPrice from ZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneTwoPrice(@Param("quantity") int quantity);

    @Query("select z.zoneThreePrice from ZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneThreePrice(@Param("quantity") int quantity);

    @Query("select z.zoneFourPrice from ZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneFourPrice(@Param("quantity") int quantity);
}
