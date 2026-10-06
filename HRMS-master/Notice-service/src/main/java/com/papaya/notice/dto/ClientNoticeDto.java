package com.papaya.notice.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClientNoticeDto {
    private Long id;
    private String noticeHeading;
    private String department;
    private String noticeDetails;
    private String addFile;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
}
