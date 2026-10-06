package com.papayaCoders.Repository;

import com.papayaCoders.model.Hr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HrRepository extends JpaRepository<Hr,Long>
{
    Optional<Hr> findByEmail(String email);

    Page<Hr> findByNameContainingIgnoreCase(String name, Pageable pageable);


}
