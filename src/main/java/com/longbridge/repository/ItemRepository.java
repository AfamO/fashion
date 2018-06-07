package com.longbridge.repository;

import com.longbridge.dto.SalesChart;
import com.longbridge.models.Items;
import com.longbridge.models.Products;
import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 21/12/2017.
 */
@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {

    List<Items> findByDesignerId(Long designerId);
    int countByDesignerIdAndDeliveryStatus(Long designerId, String status);
    Long countByMeasurementIdAndDeliveryStatusNot(Long measurementId, String status);

    int countByDesignerIdAndDeliveryStatusIn(Long designerId, List<String> status);

    @Query("select sum(quantity) from Items where designerId = :designerId and deliveryStatus = :deliveryStatus")
    int  countPendingItemQuantities(Long designerId, String status);

    int countByDesignerIdAndDeliveryStatusNotIn(Long designerId, List<String> statuses);

//    @Query("select productId, count(productId) as 'mcount', productId from Items group by productId order by 'mcount' desc")
//    Page<Long> findTopByCustomQuery(Pageable pageable);

    @Query("select sum(amount) from Items where designerId = :designerId and deliveryStatus in :deliveryStatus")
    Double findSumOfPendingOrders(@Param("designerId") Long designerId, @Param("deliveryStatus") List<String> deliveryStatus);


    //@Query(value = "SELECT SUM(amount) as amount, created_on as date FROM items WHERE designer_id =:designerid and created_on between :lastSixMonths and :current GROUP BY cast(created_on AS DATE)",nativeQuery = true)
    //List<Object[]> getSalesChart(@Param("designerid") Long designerId, @Param("lastSixMonths") Date lastSixMonths, @Param("current")Date current);


    @Query(value = "SELECT SUM(amount) as amount FROM items WHERE  designer_id =:designerid and delivery_status =:deliveryStatus and created_on between :startDate and :endDate",nativeQuery = true)
    Double getSalesChart(@Param("designerid") Long designerId, @Param("startDate") Date startDate, @Param("endDate")Date endDate, @Param("deliveryStatus") String deliveryStatus);



}
