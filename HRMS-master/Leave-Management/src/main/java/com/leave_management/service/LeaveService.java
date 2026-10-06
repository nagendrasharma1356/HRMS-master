package com.leave_management.service;


import com.leave_management.Dto.UserDto;
import com.leave_management.FeignClientService.UserClient;
import com.leave_management.common.PagedResponse;
import com.leave_management.Dto.LeaveDto;
import com.leave_management.Entity.Leave;
import com.leave_management.Enum.LeaveAction;
import com.leave_management.Repository.LeaveRepository;
import com.leave_management.exception.ResourceNotFoundException;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.leave_management.service.LeaveSpecification.filterBySessionMonthYear;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final ModelMapper modelMapper;
    private final UserClient userClient;

    private final String UPLOAD_DIR = "uploads/leave_docs/";

    public LeaveDto createLeave(LeaveDto leaveDto, MultipartFile file) throws IOException {

        // Validate if leave number exists
        if (leaveRepository.existsByLeaveNo(leaveDto.getLeaveNo())) {
            throw new RuntimeException("Leave number already exists!");
        }


        UserDto userDto;
        try {
            userDto = userClient.getUserByIdAndRole(leaveDto.getRole(), leaveDto.getUserId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("User not found in user-service with role: " + leaveDto.getRole());
        }

        //  Save file locally
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        // Calculate total days
        int totalDays = (int) ChronoUnit.DAYS.between(leaveDto.getFromDate(), leaveDto.getToDate()) + 1;

        // Map to entity
        Leave leave = modelMapper.map(leaveDto, Leave.class);
        leave.setTotal(totalDays);
        leave.setAttachment(filePath.toString());
        leave.setAction(leaveDto.getAction() != null ? leaveDto.getAction() : LeaveAction.PENDING);
        leave.setStatus(true);

        // Optional: Save user info in entity if needed
        leave.setUserId(userDto.getId());
        leave.setUserRole(userDto.getRole());

        // Save entity
        Leave savedLeave = leaveRepository.save(leave);

        //  Map back to DTO
        return modelMapper.map(savedLeave, LeaveDto.class);
    }

    public PagedResponse<LeaveDto> getAllLeaves(int page, int size, String sessionFilter, String monthFilter, Integer yearFilter) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Leave> spec = filterBySessionMonthYear(sessionFilter, monthFilter, yearFilter);

        Page<Leave> leavesPage = leaveRepository.findAll(spec, pageable);

        List<LeaveDto> content = leavesPage.getContent()
                .stream()
                .map(leave -> modelMapper.map(leave, LeaveDto.class))
                .collect(Collectors.toList());

        return PagedResponse.<LeaveDto>builder()
                .content(content)
                .pageNumber(leavesPage.getNumber())
                .pageSize(leavesPage.getSize())
                .totalElements(leavesPage.getTotalElements())
                .totalPages(leavesPage.getTotalPages())
                .last(leavesPage.isLast())
                .build();
    }

    public LeaveDto getLeaveById(Long id) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Leave not found with id: " + id));
        return modelMapper.map(leave, LeaveDto.class);
    }

    public LeaveDto updateLeave(Long id, LeaveDto leaveDto) {
        Leave existingLeave = leaveRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Leave not found with id: " + id));

        existingLeave.setLeaveNo(leaveDto.getLeaveNo());
        existingLeave.setName(leaveDto.getName());
        existingLeave.setFromDate(leaveDto.getFromDate());
        existingLeave.setToDate(leaveDto.getToDate());
        existingLeave.setReason(leaveDto.getReason());
        existingLeave.setAction(leaveDto.getAction());
        existingLeave.setStatus(leaveDto.isStatus());

        // Recalculate total days
        int totalDays = (int) ChronoUnit.DAYS.between(leaveDto.getFromDate(), leaveDto.getToDate()) + 1;
        existingLeave.setTotal(totalDays);

        Leave updatedLeave = leaveRepository.save(existingLeave);
        return modelMapper.map(updatedLeave, LeaveDto.class);
    }

    public void deleteLeaveById(Long id) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with id: " + id));

        // Delete the file if it exists
        if (leave.getAttachment() != null) {
            Path filePath = Paths.get(leave.getAttachment());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                // Optionally log error or throw custom exception if you want to stop deletion
            }
        }

        // Now delete the leave record from DB
        leaveRepository.deleteById(id);
    }

    public LeaveDto updateLeaveAction(Long id, LeaveAction newAction) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with id: " + id));

        leave.setAction(newAction);
        Leave updatedLeave = leaveRepository.save(leave);

        return modelMapper.map(updatedLeave, LeaveDto.class);
    }


}
