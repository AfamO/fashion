package com.longbridge.repository;

import com.longbridge.models.ProductNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 10/07/2018.
 */
@Repository
public interface ProductNotificationRepository extends JpaRepository<ProductNotification,Long> {
    ProductNotification findByEmailAndProductColorStyleId(String email,Long productColorStyleId);
}
