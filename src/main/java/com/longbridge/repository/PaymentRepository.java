package com.longbridge.repository;

import com.longbridge.models.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 06/08/2018.
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentRequest, Long> {
    PaymentRequest findByOrderId(Long orderId);
}
