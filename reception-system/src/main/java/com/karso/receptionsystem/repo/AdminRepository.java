package com.karso.receptionsystem.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karso.receptionsystem.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
