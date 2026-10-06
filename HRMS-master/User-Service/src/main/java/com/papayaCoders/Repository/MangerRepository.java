package com.papayaCoders.Repository;

import com.papayaCoders.model.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MangerRepository extends JpaRepository<Manager,Long>
{
    Optional<Manager>findByEmail(String email);

    Page<Manager> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
