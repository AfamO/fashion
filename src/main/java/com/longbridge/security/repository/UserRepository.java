package com.longbridge.security.repository;

import com.longbridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface UserRepository extends JpaRepository<com.longbridge.models.User, Long> {
    User findByEmail(String username);
    User findByPhoneNo(String phoneNo);
    User findById(Long Id);
    List<User> findByDesignerIsNull();

    User findByRole(String role);
}
