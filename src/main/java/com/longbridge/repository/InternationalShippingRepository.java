package com.longbridge.repository;

import com.longbridge.models.InternationalShipping;
import com.longbridge.models.LocalShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 17/10/2018.
 */
@Repository
public interface InternationalShippingRepository  extends JpaRepository<InternationalShipping, Long> {


    @Query("select s from InternationalShipping s where sending = :sending and receiving = :receiving")
    List<InternationalShipping> getPrice(@Param("sending") String sending, @Param("receiving") String receiving);
}
