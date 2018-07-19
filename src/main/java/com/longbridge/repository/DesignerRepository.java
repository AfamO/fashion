package com.longbridge.repository;

import com.longbridge.models.Designer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by longbridge on 11/5/17.
 */
public interface DesignerRepository extends JpaRepository<Designer,Long> {
    Designer findById(Long Id);
    Designer findByStoreName(String storeName);

}
