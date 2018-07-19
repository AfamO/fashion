package com.longbridge.repository;

import com.longbridge.models.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    List<Shipping> findBySendingAndReceiving(String sending, String receiving);


    @Query("select s from Shipping s where TRIM(sending) = :sending and TRIM(receiving)  = :receiving")
    List<Shipping> getPrice(@Param("sending") String sending, @Param("receiving") String receiving);

}
