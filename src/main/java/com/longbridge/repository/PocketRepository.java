package com.longbridge.repository;

import com.longbridge.models.Pocket;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 03/08/2018.
 */
@Repository
public interface PocketRepository extends JpaRepository<Pocket,Long>{
    Pocket findByUser(User user);
}
