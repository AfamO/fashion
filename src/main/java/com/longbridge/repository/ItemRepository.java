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

//    @Query("select productId, count(productId) as 'mcount', productId from Items group by productId order by 'mcount' desc")
//    Page<Long> findTopByCustomQuery(Pageable pageable);

    @Query("select sum(amount) from Items where designerId = :designerId and deliveryStatus = :deliveryStatus")
    Double findSumOfPendingOrders(@Param("designerId") Long designerId, @Param("deliveryStatus") String deliveryStatus);


    @Query(value = "SELECT SUM(amount) as amount, created_on as date FROM items WHERE designer_id =:designerid and created_on between :lastSixMonths and :current GROUP BY cast(created_on AS DATE)",nativeQuery = true)
    List<Object[]> getSalesChart(@Param("designerid") Long designerId, @Param("lastSixMonths") Date lastSixMonths, @Param("current")Date current);


//    @Query(value = "SELECT NEW com.longbridge.dto.SalesChart(SUM(s.amount), s.created_on) FROM items s WHERE s.designer_id =:designerid and s.created_on between :lastSixMonths and :current GROUP BY created_on", nativeQuery = true)
//    List<SalesChart> getSalesChart(@Param("designerid") Long designerId, @Param("lastSixMonths") Date lastSixMonths, @Param("current")Date current);




}
