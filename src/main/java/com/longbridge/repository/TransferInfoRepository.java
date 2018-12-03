package com.longbridge.repository;

import com.longbridge.models.Orders;
import com.longbridge.models.TransferInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferInfoRepository extends JpaRepository<TransferInfo, Long> {

    List<TransferInfo> findByOrders (Orders orders);
}
