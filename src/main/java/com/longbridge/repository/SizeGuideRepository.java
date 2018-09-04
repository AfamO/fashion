package com.longbridge.repository;

import com.longbridge.models.SizeGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeGuideRepository extends JpaRepository<SizeGuide, Long> {

}
