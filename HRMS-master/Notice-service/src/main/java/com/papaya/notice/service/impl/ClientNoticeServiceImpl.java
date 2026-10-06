package com.papaya.notice.service.impl;

import com.papaya.notice.Common.PageResponse;
import com.papaya.notice.dto.ClientNoticeDto;
import com.papaya.notice.entity.ClientNotice;
import com.papaya.notice.exception.ResourceNotFoundException;
import com.papaya.notice.repository.ClientNoticeRepository;
import com.papaya.notice.service.ClientNoticeService;
import com.papaya.notice.util.FileStorageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClientNoticeServiceImpl implements ClientNoticeService {

    @Autowired
    private ClientNoticeRepository clientNoticeRepository;
    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ClientNoticeDto addNotice(ClientNoticeDto dto, MultipartFile file) {

        // Extract heading & created date
        String heading = dto.getNoticeHeading().trim();
        LocalDate createdDate = (dto.getCreatedAt() != null)
                ? dto.getCreatedAt().toLocalDate()
                : LocalDate.now();

        // Check if same noticeHeading exists on same date
        boolean exists = clientNoticeRepository.existsByNoticeHeadingAndCreatedAtBetween(
                heading,
                createdDate.atStartOfDay(),
                createdDate.plusDays(1).atStartOfDay()
        );

        if (exists) {
            throw new RuntimeException("Same notice already exists on this date.");
        }

        // Map DTO to entity
        ClientNotice notice = modelMapper.map(dto, ClientNotice.class);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());

        // Save file if present
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageUtil.saveFile(file);
            notice.setAddFile(fileName);
        }

        ClientNotice saved = clientNoticeRepository.save(notice);
        return modelMapper.map(saved, ClientNoticeDto.class);
    }


    @Override
    public ClientNoticeDto updateNotice(Long id, ClientNoticeDto dto) {
        ClientNotice existingNotice = clientNoticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client Notice not found with ID: " + id));

        existingNotice.setNoticeHeading(dto.getNoticeHeading());
        existingNotice.setDepartment(dto.getDepartment());
        existingNotice.setNoticeDetails(dto.getNoticeDetails());

        if (dto.getAddFile() != null) {
            existingNotice.setAddFile(dto.getAddFile());
        }

        existingNotice.setUpdatedAt(LocalDateTime.now());

        ClientNotice updated = clientNoticeRepository.save(existingNotice);
        return modelMapper.map(updated, ClientNoticeDto.class);
    }

    // New paginated & filtered method
    public PageResponse<ClientNoticeDto> getAllNoticesPaginated(
            int page, int size,
            LocalDate date, Integer month, Integer year,
            LocalDate from, LocalDate to) {

        // Step 1: Get all notices from DB
        List<ClientNotice> allNotices = clientNoticeRepository.findAll();

        // Step 2: Apply filters using stream
        Stream<ClientNotice> noticeStream = allNotices.stream();

        if (date != null) {
            noticeStream = noticeStream.filter(n -> n.getCreatedAt().toLocalDate().equals(date));
        }

        if (month != null && year != null) {
            noticeStream = noticeStream.filter(n ->
                    n.getCreatedAt().getMonthValue() == month &&
                            n.getCreatedAt().getYear() == year);
        }

        if (from != null && to != null) {
            LocalDateTime fromDateTime = from.atStartOfDay();
            LocalDateTime toDateTime = to.plusDays(1).atStartOfDay();
            noticeStream = noticeStream.filter(n ->
                    n.getCreatedAt().isAfter(fromDateTime) &&
                            n.getCreatedAt().isBefore(toDateTime));
        }

        List<ClientNotice> filteredNotices = noticeStream.toList();

        // Step 3: Manual pagination on filtered list
        int totalElements = filteredNotices.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        List<ClientNoticeDto> paginatedDtoList = new ArrayList<>();
        if (startIndex < totalElements) {
            paginatedDtoList = filteredNotices.subList(startIndex, endIndex).stream()
                    .map(notice -> modelMapper.map(notice, ClientNoticeDto.class))
                    .collect(Collectors.toList());
        }

        boolean isLastPage = page >= totalPages - 1;

        // Step 4: Build and return custom PageResponse
        return PageResponse.<ClientNoticeDto>builder()
                .content(paginatedDtoList)
                .pageNumber(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .lastPage(isLastPage)
                .build();
    }

    @Override
    public ClientNoticeDto getNoticeById(Long id) {
        ClientNotice notice = clientNoticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client Notice not found with ID: " + id));
        return modelMapper.map(notice, ClientNoticeDto.class);
    }

    @Override
    public void deleteNotice(Long id) {
        clientNoticeRepository.deleteById(id);
    }

    @Override
    public List<ClientNoticeDto> searchByHeading(String heading) {
        return clientNoticeRepository.findByNoticeHeadingContainingIgnoreCase(heading).stream()
                .map(n -> modelMapper.map(n, ClientNoticeDto.class))
                .collect(Collectors.toList());
    }
}

