package com.papayaCoders.Service;

import com.papayaCoders.Dto.ManagerRequestDto;
import com.papayaCoders.Dto.ManagerResponseDto;
import com.papayaCoders.Repository.MangerRepository;
import com.papayaCoders.Utils.PasswordUtils;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.exception.ResourceNotFoundException;
import com.papayaCoders.model.Manager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.papayaCoders.Utils.PasswordUtils.checkPassword;

@Service
public class ManagerService {
    @Autowired
    private MangerRepository mangerRepository;

    @Autowired
    private ModelMapper mapper;

    // create
    public ApiResponse<ManagerResponseDto> create(ManagerRequestDto requestDto) {
        Optional<Manager> email = mangerRepository.findByEmail(requestDto.getEmail());
        if (email.isPresent()) {
            return new ApiResponse<>(false, "Email already exits");
        }
        Manager manager = mapper.map(requestDto, Manager.class);
        manager.setCreatedAt(LocalDate.now());
        manager.setUpdatedAt(LocalDate.now());
        manager.setPassword(PasswordUtils.hashPassword(requestDto.getPassword()));
        Manager saved = mangerRepository.save(manager);
        ManagerResponseDto mapped = mapper.map(saved, ManagerResponseDto.class);
        return new ApiResponse<>(true, "Manager Registered successfully", mapped);

    }

    //getAll
    public PagedResponse<ManagerResponseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Manager> managers = mangerRepository.findAll(pageable);
        List<ManagerResponseDto> responseDtos = managers.stream()
                .map(manager -> mapper.map(manager, ManagerResponseDto.class))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responseDtos,
                managers.getNumber(),
                managers.getSize(),
                managers.getTotalElements(),
                managers.getTotalPages(),
                managers.isLast()
        );


    }

    //getById
    public ApiResponse<ManagerResponseDto> getById(Long id) {
        Manager manager = mangerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager Not Found"));
        ManagerResponseDto responseDto = mapper.map(manager, ManagerResponseDto.class);
        return new ApiResponse<>(true, "Manager Fetched successfully", responseDto);

    }

    //getByName
    public PagedResponse<ManagerResponseDto> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Manager> byName = mangerRepository.findByNameContainingIgnoreCase(name, pageable);
        if (byName.isEmpty()) {
            return new PagedResponse<>(Collections.emptyList(), page, size, 0L, 0, true);
        }
        List<ManagerResponseDto> responseDtos = byName.stream()
                .map(manager -> mapper.map(manager, ManagerResponseDto.class))
                .collect(Collectors.toList());
        return new PagedResponse<>(
                responseDtos,
                byName.getNumber(),
                byName.getSize(),
                byName.getTotalElements(),
                byName.getTotalPages(),
                byName.isLast()
        );
    }

    // update
    public ApiResponse<ManagerResponseDto> update(Long id, ManagerRequestDto requestDto) {
        Manager manager = mangerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager Not Found"));
        if (requestDto.getName() != null) {
            manager.setName(requestDto.getName());
        }
        if (requestDto.getDepartment() != null) {
            manager.setDepartment(requestDto.getDepartment());
        }
        if (requestDto.getPhone() != null) {
            manager.setPhone(requestDto.getPhone());
        }
        Manager saved = mangerRepository.save(manager);
        ManagerResponseDto responseDto = mapper.map(saved, ManagerResponseDto.class);
        return new ApiResponse<>(true, "Manager Details update successfully", responseDto);
    }

    // delete
    public ApiResponse delete(Long id) {
        Manager manager = mangerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager Not Found"));
        mangerRepository.delete(manager);
        return new ApiResponse(true, "Manager Removed ");
    }

    // validate Manager -> Login
    public Manager validateManager(String email, String password){
        Optional<Manager> manager = mangerRepository.findByEmail(email);
        if (manager.isPresent()){
            Manager user = manager.get();
            if (checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
