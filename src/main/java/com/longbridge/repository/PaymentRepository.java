package com.longbridge.repository;

import com.longbridge.models.Payment;
import com.longbridge.models.RavePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 06/08/2018.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderId(Long orderId);
}
