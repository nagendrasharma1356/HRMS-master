package com.papayaCoders.Service;

import com.papayaCoders.Dto.HrRequestDto;
import com.papayaCoders.Dto.HrResponseDto;
import com.papayaCoders.Repository.HrRepository;
import com.papayaCoders.Utils.PasswordUtils;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.exception.ResourceNotFoundException;
import com.papayaCoders.model.Hr;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.papayaCoders.Utils.PasswordUtils.checkPassword;

@Service
public class HrService {

    @Autowired
    private HrRepository hrRepository;

    @Autowired
    private ModelMapper mapper;

    // create Hr
    public ApiResponse<HrResponseDto> createHr(HrRequestDto requestDto) {
        Optional<Hr> email = hrRepository.findByEmail(requestDto.getEmail());
        if (email.isPresent()) {
            return new ApiResponse<>(false, "Email is already Present");
        }
        Hr hr = mapper.map(requestDto, Hr.class);
        hr.setCreatedAt(LocalDate.now());
        hr.setUpdatedAt(LocalDate.now());
        hr.setPassword(PasswordUtils.hashPassword(requestDto.getPassword()));

        Hr saved = hrRepository.save(hr);
        HrResponseDto responseDto = mapper.map(saved, HrResponseDto.class);
        return new ApiResponse<>(true, "Hr Register successfully", responseDto);

    }

    // get all
    public PagedResponse<HrResponseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Hr> hr = hrRepository.findAll(pageable);
        List<HrResponseDto> responseDtos = hr.stream()
                .map(hr1 -> mapper.map(hr1, HrResponseDto.class))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responseDtos,
                hr.getNumber(),
                hr.getSize(),
                hr.getTotalElements(),
                hr.getTotalPages(),
                hr.isLast()
        );
    }

    //get by id
    public ApiResponse<HrResponseDto> getById(Long id) {
        Hr hr = hrRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hr not found"));
        HrResponseDto responseDto = mapper.map(hr, HrResponseDto.class);
        return new ApiResponse<>(true, "Hr fetched successfully", responseDto);
    }

    // update hr
    public ApiResponse<HrResponseDto> updateHr(Long id, HrRequestDto requestDto) {
        Hr hr = hrRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hr not found"));
        Optional<Hr> email = hrRepository.findByEmail(requestDto.getEmail());
        if (email.isPresent()){
            return new ApiResponse<>(false,"Email is already exists");
        }
        if (requestDto.getName() != null) {
            hr.setName(requestDto.getName());
        }
        if (requestDto.getDepartment() != null) {
            hr.setDepartment(requestDto.getDepartment());
        }
        if (requestDto.getPosition() != null) {
            hr.setPosition(requestDto.getPosition());
        }
        if (requestDto.getPhone() != null) {
            hr.setPhone(requestDto.getPhone());
        }
        hr.setUpdatedAt(LocalDate.now());
        Hr saved = hrRepository.save(hr);
        HrResponseDto responseDto = mapper.map(saved, HrResponseDto.class);
        return new ApiResponse<>(true, "Update successfully", responseDto);
    }

    // search by name
    public PagedResponse<HrResponseDto> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Hr> byName = hrRepository.findByNameContainingIgnoreCase(name, pageable);

        if (byName.isEmpty()) {
            return new PagedResponse<>(Collections.emptyList(), page, size, 0L, 0, true);
        }

        List<HrResponseDto> content = byName.stream()
                .map(byNames -> mapper.map(byNames, HrResponseDto.class)) // Assuming you have a method mapToDto(Hr hr)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                byName.getNumber(),
                byName.getSize(),
                byName.getTotalElements(),
                byName.getTotalPages(),
                byName.isLast()
        );
    }


    // delete Hr
    public ApiResponse delete(Long id){
        Hr hr = hrRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hr not found"));
        hrRepository.delete(hr);
        return new ApiResponse<>(true,"Hr removed successfully");
    }

    // validate Hr
    public Hr validateUser(String email, String password){
        Optional<Hr> hr = hrRepository.findByEmail(email);
        if (hr.isPresent()) {
            Hr user = hr.get();

            if (checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

}
