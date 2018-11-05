package com.longbridge.repository;

import com.longbridge.models.ProductStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 25/10/2018.
 */
@Repository
public interface ProductStatusesRepository extends JpaRepository<ProductStatuses,Long> {
}
