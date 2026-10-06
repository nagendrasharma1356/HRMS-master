package com.papaya.notice.service;

import com.papaya.notice.dto.EmployeeNoticeDto;
import com.papaya.notice.Common.PageResponse;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.List;
public interface EmployeeNoticeService {

    EmployeeNoticeDto addNotice(EmployeeNoticeDto dto, MultipartFile file);


    EmployeeNoticeDto updateNotice(Long id, EmployeeNoticeDto dto);

    // Pagination method
    PageResponse<EmployeeNoticeDto> getAllNoticesPaginated(
            int page, int size,
            LocalDate date, Integer month, Integer year,
            LocalDate from, LocalDate to
    );

    EmployeeNoticeDto getNoticeById(Long id);

    void deleteNotice(Long id);

    List<EmployeeNoticeDto> searchByHeading(String heading);
}
