package com.longbridge.repository;

import com.longbridge.models.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
@Repository
public interface EventRepository extends CommonRepository<Events,Long> {
    List<Events> findTop5ByOrderByEventDateDesc();
    Page<Events> findAllByOrderByTrendingCountDesc(Pageable pageable);
    Page<Events> findByEventDateBetweenOrderByEventDateDesc(Date startDate, Date endDate, Pageable pageData);
    Page<Events> findByEventType(String eventType, Pageable pageable);

//    @Query("SELECT e from Events  e where eventsName like %:search%")
//    List<Events> searchBySearchTerm(@Param("search") String search);

}
