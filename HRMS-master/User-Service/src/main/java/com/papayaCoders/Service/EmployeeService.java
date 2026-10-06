package com.papayaCoders.Service;

import com.papayaCoders.Dto.EmployeeRequestDto;
import com.papayaCoders.Dto.EmployeeResponseDto;
import com.papayaCoders.Repository.EmployeeRepository;
import com.papayaCoders.Utils.PasswordUtils;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.common.PagedResponse;
import com.papayaCoders.exception.ResourceNotFoundException;
import com.papayaCoders.model.Employee;
import com.papayaCoders.model.Hr;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.papayaCoders.Utils.PasswordUtils.checkPassword;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper mapper;

    // create
    public ApiResponse<EmployeeResponseDto> create(EmployeeRequestDto requestDto) {
        Optional<Employee> email = employeeRepository.findByEmail(requestDto.getEmail());
        if (email.isPresent()) {
            return new ApiResponse<>(false, "Email is already exists");
        }
        Optional<Employee> aadhaarNumber = employeeRepository.findByAadhaarNumber(requestDto.getAadhaarNumber());
        if (aadhaarNumber.isPresent()) {
            return new ApiResponse<>(false, "Aadhaar Number is already exists");
        }
        Employee employee = mapper.map(requestDto, Employee.class);
        employee.setCreatedAt(LocalDate.now());
        employee.setUpdatedAt(LocalDate.now());
        employee.setPassword(PasswordUtils.hashPassword(requestDto.getPassword()));
        Employee saved = employeeRepository.save(employee);
        EmployeeResponseDto responseDto = mapper.map(saved, EmployeeResponseDto.class);
        return new ApiResponse<>(true, "Employee Registered successfully",responseDto);

    }

    // getAll
    public PagedResponse<EmployeeResponseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<EmployeeResponseDto> responseDtos = employees.stream()
                .map(employee -> mapper.map(employee, EmployeeResponseDto.class))
                .collect(Collectors.toList());
        return new PagedResponse<>(
                responseDtos,
                employees.getNumber(),
                employees.getSize(),
                employees.getTotalElements(),
                employees.getTotalPages(),
                employees.isLast()
        );
    }

    //getById
    public ApiResponse<EmployeeResponseDto> getById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not Found"));
        EmployeeResponseDto responseDto = mapper.map(employee, EmployeeResponseDto.class);
        return new ApiResponse<>(true, "Employee fetched successfully", responseDto);
    }

    // searchByName
    public PagedResponse<EmployeeResponseDto> searchByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeRepository.findByNameContainingIgnoreCase(name, pageable);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("Employee Not Found");
        }
        List<EmployeeResponseDto> responseDtos = employees.stream()
                .map(employee -> mapper.map(employee, EmployeeResponseDto.class))
                .collect(Collectors.toList());
        return new PagedResponse<>(
                responseDtos,
                employees.getNumber(),
                employees.getSize(),
                employees.getTotalElements(),
                employees.getTotalPages(),
                employees.isLast()

        );
    }

    //searchByAadhaarCard
    public ApiResponse<EmployeeResponseDto> searchByAadhaar(Long aadhaarNumber) {
        Optional<Employee> aadhaarNumber1 = employeeRepository.findByAadhaarNumber(aadhaarNumber);
        if (aadhaarNumber1.isEmpty()) {
            return new ApiResponse<>(false, "Employee not found");
        }
        EmployeeResponseDto responseDto = mapper.map(aadhaarNumber1, EmployeeResponseDto.class);
        return new ApiResponse<>(true, "Employee Fetched successfully",responseDto);
    }

    //update
    public ApiResponse<EmployeeResponseDto> update(Long id, EmployeeRequestDto requestDto) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not Found"));
        if (requestDto.getName() != null) {
            employee.setName(requestDto.getName());
        }
        if (requestDto.getPhone() != null) {
            employee.setPhone(requestDto.getPhone());
        }
        if (requestDto.getDepartment() != null) {
            employee.setDepartment(requestDto.getDepartment());
        }
        if (requestDto.getPosition() != null) {
            employee.setPosition(requestDto.getPosition());
        }
        if(requestDto.getSalary()!=null){
            employee.setSalary(requestDto.getSalary());
        }
        if (requestDto.getDateOfJoining()!=null){
            employee.setDateOfJoining(requestDto.getDateOfJoining());
        }
        if(requestDto.getGender()!=null){
            employee.setGender(requestDto.getGender());
        }
        if(requestDto.getDateOfBirth()!=null){
            employee.setDateOfBirth(requestDto.getDateOfBirth());
        }
        if(requestDto.getReportingManager()!=null){
            employee.setReportingManager(requestDto.getReportingManager());
        }
        if (requestDto.getPanNumber()!=null){
            employee.setPanNumber(requestDto.getPanNumber());
        }
        if(requestDto.getMaritalStatus()!=null){
            employee.setMaritalStatus(requestDto.getMaritalStatus());
        }
        employee.setUpdatedAt(LocalDate.now());
        Employee saved = employeeRepository.save(employee);
        EmployeeResponseDto responseDto = mapper.map(saved, EmployeeResponseDto.class);
        return new ApiResponse<>(true,"Employee details update successfully");
    }

    //delete
    public ApiResponse delete(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Not Found"));
        employeeRepository.delete(employee);
        return new ApiResponse(true,"Employee removed");

    }

    // validate
    public Employee validateUser(String email, String password){
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isPresent()) {
            Employee user = employee.get();
            // Check if the provided password matches the stored hashed password
            if (checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }



}
