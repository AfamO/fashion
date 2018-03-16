package com.longbridge.repository;

import com.longbridge.models.Measurement;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 12/03/2018.
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement,Long> {
    Measurement findByUserAndName(User user, String name);
    List<Measurement> findByUser(User user);

}
