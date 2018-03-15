package com.longbridge.repository;


import com.longbridge.models.MailError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Longbridge on 28/02/2018.
 */
@Repository
public interface MailErrorRepository extends JpaRepository<MailError,Long> {
    List<MailError> findByDelFlag(String delFlag);
}
