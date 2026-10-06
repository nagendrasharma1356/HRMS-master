package com.leave_management.Repository;

import com.leave_management.Entity.Leave;
import com.leave_management.Enum.LeaveAction;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> , JpaSpecificationExecutor<Leave> {
    boolean existsByLeaveNo(String leaveNo);
    @Query("SELECT FUNCTION('MONTH', l.fromDate), l.action, SUM(l.Total) " +
            "FROM Leave l " +
            "WHERE l.name = :name AND FUNCTION('YEAR', l.fromDate) = :year " +
            "GROUP BY FUNCTION('MONTH', l.fromDate), l.action")
    List<Object[]> getMonthlyLeaveSummary(@Param("name") String name, @Param("year") int year);

    List<Leave> findByUserIdAndUserRole(Long userId, String userRole);

    @Query("SELECT l FROM Leave l WHERE l.userId = :userId AND l.userRole = :userRole AND MONTH(l.fromDate) = :month AND YEAR(l.fromDate) = :year AND l.action = :action")
    List<Leave> findApprovedLeavesByUserIdAndUserRoleAndMonth(
            @Param("userId") Long userId,
            @Param("userRole") String userRole,
            @Param("month") int month,
            @Param("year") int year,
            @Param("action") LeaveAction action);




}
