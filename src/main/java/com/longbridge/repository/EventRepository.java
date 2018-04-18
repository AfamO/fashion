package com.longbridge.repository;

import com.longbridge.models.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
@Repository
public interface EventRepository extends PagingAndSortingRepository<Events,Long> {
    List<Events> findTop5ByOrderByEventDateDesc();
    Page<Events> findAllByOrderByTrendingCountDesc(Pageable pageable);
    Page<Events> findByEventDateBetween(Date startDate, Date endDate, Pageable pageData);
    Page<Events> findByEventType(String eventType, Pageable pageable);


}
