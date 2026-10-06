package com.payroll.Repository;

import com.payroll.Entity.AssignedPayroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssignedPayrollRepository extends JpaRepository<AssignedPayroll, Long> {
    boolean existsByPayrollName(String payrollName);
    Optional<AssignedPayroll> findByUserIdAndUserRole(Long userId, String userRole);

    @Query("SELECT a FROM AssignedPayroll a WHERE " +
            "(:month IS NULL OR MONTH(a.assignedAt) = :month) AND " +
            "(:year IS NULL OR YEAR(a.assignedAt) = :year)")
    Page<AssignedPayroll> findAllByMonthAndYear(@Param("month") Integer month,
                                                @Param("year") Integer year,
                                                Pageable pageable);

}
