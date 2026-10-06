package com.papaya.notice.service.impl;

import com.papaya.notice.Common.PageResponse;
import com.papaya.notice.dto.EmployeeNoticeDto;
import com.papaya.notice.entity.EmployeeNotice;
import com.papaya.notice.exception.ResourceNotFoundException;
import com.papaya.notice.repository.EmployeeNoticeRepository;
import com.papaya.notice.service.EmployeeNoticeService;
import com.papaya.notice.util.FileStorageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeNoticeServiceImpl implements EmployeeNoticeService {

    @Autowired
    private EmployeeNoticeRepository employeeNoticeRepository;
    @Autowired
    private FileStorageUtil fileStorageUtil;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EmployeeNoticeDto addNotice(EmployeeNoticeDto dto, MultipartFile file) {

        // Trim heading + extract date (if null, use today)
        String heading = dto.getNoticeHeading().trim();
        LocalDate createdDate = (dto.getCreatedAt() != null)
                ? dto.getCreatedAt().toLocalDate()
                : LocalDate.now();
        // Check if same heading exists for same date
        boolean exists = employeeNoticeRepository.existsByNoticeHeadingAndCreatedAtBetween(
                heading,
                createdDate.atStartOfDay(),
                createdDate.plusDays(1).atStartOfDay()
        );

        if (exists) {
            throw new RuntimeException("Same employee notice already exists on this date.");
        }

        EmployeeNotice notice = modelMapper.map(dto, EmployeeNotice.class);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());

        //  Save file (if provided)
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageUtil.saveFile(file);
            notice.setAddFile(fileName);
        }

        EmployeeNotice saved = employeeNoticeRepository.save(notice);
        return modelMapper.map(saved, EmployeeNoticeDto.class);
    }



    // Updated method with pagination and filtering
    public PageResponse<EmployeeNoticeDto> getAllNoticesPaginated(
            int page, int size,
            LocalDate date, Integer month, Integer year,
            LocalDate from, LocalDate to) {

        List<EmployeeNotice> allNotices = employeeNoticeRepository.findAll();

        // Filter manually
        List<EmployeeNotice> filtered = allNotices;

        if (date != null) {
            filtered = filtered.stream()
                    .filter(n -> n.getCreatedAt().toLocalDate().equals(date))
                    .collect(Collectors.toList());
        }
        if (month != null && year != null) {
            filtered = filtered.stream()
                    .filter(n -> n.getCreatedAt().getMonthValue() == month &&
                            n.getCreatedAt().getYear() == year)
                    .collect(Collectors.toList());
        }
        if (from != null && to != null) {
            LocalDateTime fromDateTime = from.atStartOfDay();
            LocalDateTime toDateTime = to.plusDays(1).atStartOfDay();
            filtered = filtered.stream()
                    .filter(n -> n.getCreatedAt().isAfter(fromDateTime) &&
                            n.getCreatedAt().isBefore(toDateTime))
                    .collect(Collectors.toList());
        }

        // Manual pagination
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, filtered.size());

        List<EmployeeNoticeDto> paginatedList = new ArrayList<>();
        if (startIndex < filtered.size()) {
            paginatedList = filtered.subList(startIndex, endIndex).stream()
                    .map(n -> modelMapper.map(n, EmployeeNoticeDto.class))
                    .collect(Collectors.toList());
        }

        int totalElements = filtered.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean isLastPage = page == totalPages - 1;

        return PageResponse.<EmployeeNoticeDto>builder()
                .content(paginatedList)
                .pageNumber(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .lastPage(isLastPage)
                .build();
    }


    // Update notice
    @Override
    public EmployeeNoticeDto updateNotice(Long id, EmployeeNoticeDto dto) {
        EmployeeNotice existingNotice = employeeNoticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Notice not found with ID: " + id));

        existingNotice.setNoticeHeading(dto.getNoticeHeading());
        existingNotice.setDepartment(dto.getDepartment());
        existingNotice.setNoticeDetails(dto.getNoticeDetails());
        if (dto.getAddFile() != null) {
            existingNotice.setAddFile(dto.getAddFile());
        }
        existingNotice.setUpdatedAt(LocalDateTime.now());

        EmployeeNotice updated = employeeNoticeRepository.save(existingNotice);
        return modelMapper.map(updated, EmployeeNoticeDto.class);
    }

    @Override
    public EmployeeNoticeDto getNoticeById(Long id) {
        EmployeeNotice notice = employeeNoticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Notice not found with ID: " + id));
        return modelMapper.map(notice, EmployeeNoticeDto.class);
    }

    @Override
    public void deleteNotice(Long id) {
        employeeNoticeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeNoticeDto> searchByHeading(String heading) {
        return employeeNoticeRepository.findByNoticeHeadingContainingIgnoreCase(heading).stream()
                .map(n -> modelMapper.map(n, EmployeeNoticeDto.class))
                .collect(Collectors.toList());
    }


}
