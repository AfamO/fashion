package com.longbridge.repository;

import com.longbridge.models.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {

    ItemStatus findByStatus(String status);
    List<ItemStatus> findByStatusIn(List<String> statuses);

}
