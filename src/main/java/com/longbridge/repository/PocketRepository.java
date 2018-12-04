package com.longbridge.repository;

import com.longbridge.models.ItemStatus;
import com.longbridge.models.Pocket;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 03/08/2018.
 */
@Repository
public interface PocketRepository extends JpaRepository<Pocket,Long>{
    //Pocket findByUser(User user);
    List<Pocket> findByUser(User user);
    Pocket findByUserAndItemId(User user, Long itemId);


    @Query("select sum(balance) from Pocket where user = :user and debitFlag = :debitFlag")
    Double getPocketSum(@Param("user") User user, @Param("debitFlag") String debitFlag);

    List<Pocket> findByDueDateForDebitIsLessThanEqualAndDebitFlag(Date date, String flag);
}
