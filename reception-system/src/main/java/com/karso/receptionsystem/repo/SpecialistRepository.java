package com.karso.receptionsystem.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karso.receptionsystem.model.Specialist;

public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    
    Optional<Specialist> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Specialist> findByDepartment(String department);
    
    List<Specialist> findByIsActiveTrue();
}
