package com.longbridge.repository;

import com.longbridge.dto.SalesChart;
import com.longbridge.models.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Created by longbridge on 11/5/17.
 */
public interface DesignerRepository extends JpaRepository<Designer,Long> {
    Designer findById(Long Id);
    Designer findByStoreName(String storeName);

}
