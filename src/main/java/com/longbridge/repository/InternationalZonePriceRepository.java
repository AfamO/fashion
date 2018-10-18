package com.longbridge.repository;

import com.longbridge.models.InternationalZonePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 17/10/2018.
 */
@Repository
public interface InternationalZonePriceRepository extends JpaRepository<InternationalZonePrice, Long> {
    @Query("select z.zoneOnePrice from InternationalZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneOnePrice(@Param("quantity") int quantity);

    @Query("select z.zoneTwoPrice from InternationalZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneTwoPrice(@Param("quantity") int quantity);

    @Query("select z.zoneThreePrice from InternationalZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneThreePrice(@Param("quantity") int quantity);

    @Query("select z.zoneFourPrice from InternationalZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneFourPrice(@Param("quantity") int quantity);

    @Query("select z.zoneFivePrice from InternationalZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneFivePrice(@Param("quantity") int quantity);

    @Query("select z.zoneSixPrice from InternationalZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneSixPrice(@Param("quantity") int quantity);

    @Query("select z.zoneSevenPrice from InternationalZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneSevenPrice(@Param("quantity") int quantity);

    @Query("select z.zoneEightPrice from InternationalZonePrice z where z.fromQuantity <= :quantity and z.toQuantity >= :quantity")
    Double getZoneEightPrice(@Param("quantity") int quantity);

}
