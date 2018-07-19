package com.longbridge.repository;

import com.longbridge.models.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    List<Shipping> findBySendingAndReceiving(String sending, String receiving);


    @Query("select s from Shipping s where sending = :sending and receiving = :receiving")
    List<Shipping> getPrice(String sending, String receiving);

}
