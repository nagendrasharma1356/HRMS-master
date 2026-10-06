package com.papayaCoders.Repository;

import com.papayaCoders.model.SalaryDeductionRecord;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryDeductionRecordRepository extends JpaRepository<SalaryDeductionRecord, Long> {

    boolean existsByUserIdAndLeaveDate(Long userId, LocalDate leaveDate);

    List<SalaryDeductionRecord> findAllByUserIdAndLeaveDateIsNotNull(Long userId);

    Optional<SalaryDeductionRecord> findNetSalaryRecordByUserId(Long userId);
}
