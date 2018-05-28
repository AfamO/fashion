package com.longbridge.repository;

import com.longbridge.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 21/12/2017.
 */
@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findByOrderNum(String orderNum);
   // Orders findByItem(Lon item);
    List<Orders> findByUserId(Long userId);

    @Query("select sum(totalAmount) from Orders where userId = :userId")
    Double getWalletBalance(Long userId);


}
