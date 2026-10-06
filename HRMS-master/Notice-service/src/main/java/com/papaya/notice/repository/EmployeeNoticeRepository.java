package com.papaya.notice.repository;

import com.papaya.notice.entity.EmployeeNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeNoticeRepository extends JpaRepository<EmployeeNotice, Long> {

    // For searching from UI
    List<EmployeeNotice> findByNoticeHeadingContainingIgnoreCase(String noticeHeading);

    // For duplicate check (same heading, same day)
    boolean existsByNoticeHeadingAndCreatedAtBetween(String noticeHeading, LocalDateTime start, LocalDateTime end);
}
