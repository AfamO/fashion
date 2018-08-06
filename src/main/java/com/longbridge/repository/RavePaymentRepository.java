package com.longbridge.repository;

import com.longbridge.models.RavePayment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Longbridge on 06/08/2018.
 */
public interface RavePaymentRepository extends JpaRepository<RavePayment, Long> {
    RavePayment findByOrderId(Long orderId);
}
