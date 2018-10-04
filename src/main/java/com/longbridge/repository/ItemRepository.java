package com.longbridge.repository;

import com.longbridge.dto.ISalesChart;
import com.longbridge.dto.SalesChart;
import com.longbridge.models.ItemStatus;
import com.longbridge.models.Items;
import com.longbridge.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 21/12/2017.
 */
@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {

    List<Items> findByDesignerId(Long designerId);
    List<Items> findByDesignerIdAndItemStatusOrderByOrders_OrderDateDesc(Long designerId, ItemStatus status);

    List<Items> findByItemStatusNotOrderByOrders_OrderDateDesc(ItemStatus status);
    List<Items> findByDesignerIdAndItemStatusNotInOrderByOrders_OrderDateDesc(Long designerId,List<ItemStatus> status);

    List<Items> findByDesignerIdAndItemStatus(Long designerId, ItemStatus status);


    List<Items> findByItemStatusInOrderByOrders_OrderDateDesc(List<ItemStatus> status);

    List<Items> findByItemStatus_StatusInOrderByOrders_OrderDateDesc(List<String> status);

    //List<Items> countByDesignerIdAndItemStatus(Long designerId, ItemStatus status);

    int countByDesignerId(Long designerId);
    int countByDesignerIdAndItemStatus_Status(Long designerId, String status);
    int countByDesignerIdAndItemStatus_StatusIn(Long designerId, List<String> status);
    Long countByMeasurementIdAndItemStatusNot(Long measurementId, String status);


    @Query("select i from Items i where designerId = :designerId and itemStatus in :itemStatuses order by orders.orderDate Desc")
    List<Items> findActiveOrders(@Param("designerId") Long designerId, @Param("itemStatuses") List<ItemStatus> itemStatuses);

    @Query("select count(id) from Items i where designerId = :designerId and productId =:productId and itemStatus in :itemStatuses")
    int findActiveOrdersOnProduct(@Param("designerId") Long designerId, @Param("productId") Long productId, @Param("itemStatuses") List<ItemStatus> itemStatuses);

    int countByDesignerIdAndProductIdAndItemStatus_Status(Long designerId, Long productId, String status);

    int countByDesignerIdAndDeliveryStatusIn(Long designerId, List<String> status);

    @Query("select sum(quantity) from Items where designerId = :designerId and deliveryStatus = :deliveryStatus")
    int  countPendingItemQuantities(Long designerId, String status);

    List<Items> findByOrders(Orders orders);

    @Query("select sum(amount) from Items where designerId = :designerId and itemStatus in :itemStatus")
    Double findSumOfPendingOrders(@Param("designerId") Long designerId, @Param("itemStatus") List<ItemStatus> itemStatus);

    @Query("select sum(amount) from Items where designerId = :designerId and itemStatus not in :itemStatus")
    Double findSumOfOrders(@Param("designerId") Long designerId,  @Param("itemStatus") List<ItemStatus> itemStatus);


    @Query(value = "SELECT SUM(amount) as amount,  MONTH(created_on) as month ,YEAR(created_on) as year FROM items WHERE  " +
            "designer_id =:designerid and delivery_status =:deliveryStatus and created_on between :startDate and current_date() " +
            "GROUP BY MONTH(created_on)",nativeQuery = true)
    List<ISalesChart> getTotalSales(@Param("designerid") Long designerId, @Param("startDate") Date startDate, @Param("deliveryStatus") String deliveryStatus);


}
