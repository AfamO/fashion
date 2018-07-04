package com.longbridge.repository;

import com.longbridge.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 03/07/2018.
 */
@Repository
public interface SizeRepository extends JpaRepository<Size,Long>{
}
