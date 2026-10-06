package com.payroll.Service;

import com.payroll.Dto.PayrollDto;

import com.payroll.enumClass.PayrollType;
import com.payroll.enumClass.Status;
import com.payroll.Dto.UserDto;
import com.payroll.Entity.Payroll;
import com.payroll.Repository.PayrollRepository;
import com.payroll.common.PagedResponse;
import com.payroll.exception.ResourceNotFoundException;
import feign.FeignException;
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

@Service
public class PayrollService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PayrollRepository payrollRepository;


    // create
    public PayrollDto create(PayrollDto payrollDto) {
        Payroll payroll = mapper.map(payrollDto, Payroll.class);
        if (payrollDto.getType() == PayrollType.ALLOWANCE) {
            payroll.setAction("Credit");
        } else if (payrollDto.getType() == PayrollType.DEDUCTION) {
            payroll.setAction("Debit");
        } else {
            payroll.setAction("PENDING");
        }

        payroll.setCreatedAt(LocalDate.now());
        payroll.setUpdatedAt(LocalDate.now());
        payroll.setStatus(Status.UNPAID);


        Payroll saved = payrollRepository.save(payroll);
        return mapper.map(saved, PayrollDto.class);
    }

    // getAll
    public PagedResponse<PayrollDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payroll> payrolls = payrollRepository.findAll(pageable);
        List<PayrollDto> collect = payrolls.stream()
                .map(payroll -> mapper.map(payroll, PayrollDto.class))
                .collect(Collectors.toList());
        return PagedResponse.<PayrollDto>builder()
                .content(collect)
                .pageNumber(payrolls.getNumber())
                .pageSize(payrolls.getSize())
                .totalElements(payrolls.getTotalElements())
                .last(payrolls.isLast())
                .build();
    }

    // getById
    public PayrollDto getById(Long id){
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        PayrollDto map = mapper.map(payroll, PayrollDto.class);
        return map;
    }

    // update
    public PayrollDto update(Long id, PayrollDto payrollDto) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found"));

        payroll.setName(payrollDto.getName());
        payroll.setPercentage(payrollDto.getPercentage());
        payroll.setFixedAmount(payrollDto.getFixedAmount());
        payroll.setUpdatedAt(LocalDate.now());

        // Update action if type is modified
        if (payrollDto.getType() == PayrollType.ALLOWANCE) {
            payroll.setAction("Credit");
        } else if (payrollDto.getType() == PayrollType.DEDUCTION) {
            payroll.setAction("Debit");
        }


        payroll.setType(payrollDto.getType());

        Payroll saved = payrollRepository.save(payroll);
        return mapper.map(saved, PayrollDto.class);
    }

    // delete
    public void delete(Long id){
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        payrollRepository.delete(payroll);
    }

    // status update
    public PayrollDto updateStatus(Long id, Status newStatus) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found"));

        payroll.setStatus(newStatus);
        payroll.setUpdatedAt(LocalDate.now());

        Payroll updated = payrollRepository.save(payroll);
        return mapper.map(updated, PayrollDto.class);
    }






}
