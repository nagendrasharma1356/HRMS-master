package com.papaya.notice.service;

import com.papaya.notice.Common.PageResponse;
import com.papaya.notice.dto.ClientNoticeDto;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.List;
public interface ClientNoticeService {

    ClientNoticeDto addNotice(ClientNoticeDto dto, MultipartFile file);
    ClientNoticeDto updateNotice(Long id, ClientNoticeDto dto);

    PageResponse<ClientNoticeDto> getAllNoticesPaginated(
            int page, int size,
            LocalDate date, Integer month, Integer year,
            LocalDate from, LocalDate to
    );

    ClientNoticeDto getNoticeById(Long id);
    void deleteNotice(Long id);
    List<ClientNoticeDto> searchByHeading(String heading);
}
