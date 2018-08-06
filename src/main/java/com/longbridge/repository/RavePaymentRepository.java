package com.longbridge.repository;

import com.longbridge.models.RavePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 06/08/2018.
 */
@Repository
public interface RavePaymentRepository extends JpaRepository<RavePayment, Long> {
    RavePayment findByOrderId(Long orderId);
}
