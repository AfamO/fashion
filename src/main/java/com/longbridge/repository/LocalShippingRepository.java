package com.longbridge.repository;

import com.longbridge.models.LocalShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalShippingRepository extends JpaRepository<LocalShipping, Long> {

    List<LocalShipping> findBySendingAndReceiving(String sending, String receiving);


    @Query("select s from LocalShipping s where sending = :sending and receiving = :receiving")
    List<LocalShipping> getPrice(@Param("sending") String sending, @Param("receiving") String receiving);

}
