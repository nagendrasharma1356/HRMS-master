package com.papaya.notice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String noticeHeading;
    private String Department;
    @Column(length = 2000)
    private String noticeDetails;
    private String addFile;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;

}
