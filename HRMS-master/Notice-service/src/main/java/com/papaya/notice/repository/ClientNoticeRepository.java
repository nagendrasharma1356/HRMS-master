package com.papaya.notice.repository;

import com.papaya.notice.entity.ClientNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientNoticeRepository extends JpaRepository<ClientNotice, Long> {

    // Search by partial heading (for UI search etc.)
    List<ClientNotice> findByNoticeHeadingContainingIgnoreCase(String noticeHeading);

    //Check if same noticeHeading exists within a specific date range (same day)
    boolean existsByNoticeHeadingAndCreatedAtBetween(String noticeHeading, LocalDateTime start, LocalDateTime end);
}
