package com.longbridge.repository;

import com.longbridge.models.StatusResponseMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusResponseMessageRepository extends JpaRepository<StatusResponseMessage, Long> {

}
