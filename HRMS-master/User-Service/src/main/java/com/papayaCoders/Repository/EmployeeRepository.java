package com.papayaCoders.Repository;


import com.papayaCoders.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
    Optional<Employee>findByAadhaarNumber(Long aadhaarNumber);
    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
