package com.longbridge.repository;

import com.longbridge.models.Orders;
import com.longbridge.models.TransferInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferInfoRepository extends JpaRepository<TransferInfo, Long> {

    TransferInfo findByOrders (Orders orders);
}
