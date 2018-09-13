package com.longbridge.repository;

import com.longbridge.models.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {

    List<Code> findByType(String type);
    Code findByNameAndType(String name, String type);
}
