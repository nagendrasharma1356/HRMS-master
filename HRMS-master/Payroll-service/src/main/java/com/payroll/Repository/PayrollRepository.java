package com.payroll.Repository;

import com.payroll.Entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRepository extends JpaRepository<Payroll,Long> {
}
