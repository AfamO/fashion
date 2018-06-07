package com.longbridge.repository;

import com.longbridge.models.Items;
import com.longbridge.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT Count(id) FROM Orders WHERE (id IN (SELECT orders FROM Items WHERE productId = :productId)) AND userId = :userId")
    Long noOfTimesOrdered(@Param("productId") Long productId, @Param("userId") Long userId);


}
