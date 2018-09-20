package com.longbridge.repository;

import com.longbridge.models.OrderFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 20/09/2018.
 */
@Repository
public interface OrderFeedBackRepository extends JpaRepository<OrderFeedBack,Long>{


}
