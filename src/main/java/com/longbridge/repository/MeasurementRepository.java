package com.longbridge.repository;

import com.longbridge.models.Measurement;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 12/03/2018.
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement,Long> {
    Measurement findByUserAndNameAndDelFlag(User user, String name, String delFlag);
    List<Measurement> findByUserAndDelFlag(User user, String delFlag);
}
