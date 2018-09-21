package com.longbridge.repository;

import com.longbridge.models.Designer;
import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by longbridge on 11/5/17.
 */
public interface DesignerRepository extends JpaRepository<Designer,Long> {
    Designer findById(Long Id);
    Designer findByStoreName(String storeName);

    Designer findByUser(User user);

    List<Designer> findTop10ByOrderByCreatedOnDesc();

    Designer findByUser_Email(String name);
}
