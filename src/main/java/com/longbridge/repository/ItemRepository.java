package com.longbridge.repository;

import com.longbridge.models.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 21/12/2017.
 */
@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {
    List<Items> findByDesignerId(Long designerId);

}
