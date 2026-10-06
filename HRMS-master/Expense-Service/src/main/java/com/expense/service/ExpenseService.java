package com.expense.service;

import com.expense.common.PagedResponse;
import com.expense.dto.ExpenseCategoryDto;
import com.expense.dto.ExpenseDto;
import com.expense.entity.Expense;
import com.expense.entity.ExpenseCategory;
import com.expense.exception.ResourceNotFoundException;
import com.expense.repository.ExpenseCategoryRepository;
import com.expense.repository.ExpenseRepository;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseCategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    // create
    public ExpenseDto createExpense(ExpenseDto dto) throws BadRequestException {
        if (expenseRepository.existsByTitle(dto.getTitle())) {
            throw new BadRequestException("Expense with title '" + dto.getTitle() + "' already exists"); // ✅ correct

        }

        Expense expense = mapper.map(dto, Expense.class);
        ExpenseCategory category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
        expense.setCategory(category);
        expense.setCreatedAt(LocalDate.now());
        expense.setUpdatedAt(LocalDate.now());
        Expense saved = expenseRepository.save(expense);
        return mapper.map(saved, ExpenseDto.class);
    }

    // getAll
    public PagedResponse<ExpenseDto> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Expense> expenses = expenseRepository.findAll(pageable);
        List<ExpenseDto> collect = expenses.stream()
                .map(expense -> mapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());
        double totalAmount = collect.stream()
                .mapToDouble(ExpenseDto::getAmount)
                .sum();

        return PagedResponse.<ExpenseDto>builder()
                .content(collect)
                .pageNumber(expenses.getNumber())
                .pageSize(expenses.getSize())
                .totalElements(expenses.getTotalElements())
                .last(expenses.isLast())
                .totalAmount(totalAmount)
                .build();
    }

    //getById
    public ExpenseDto getById(Long id){
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        ExpenseDto map = mapper.map(expense, ExpenseDto.class);
        return map;
    }

    //update
    public ExpenseDto update(Long id, ExpenseDto expenseDto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", id));

        ExpenseCategory category = categoryRepository.findById(expenseDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpenseCategory", "id", expenseDto.getCategoryId()));

        // Update fields
        expense.setDescription(expenseDto.getDescription());
        expense.setDate(new Date());
        expense.setAmount(expenseDto.getAmount());
        expense.setTitle(expenseDto.getTitle());
        expense.setReferenceNo(expenseDto.getReferenceNo());
        expense.setSessionYear(expenseDto.getSessionYear());
        expense.setCategory(category);
        expense.setUpdatedAt(LocalDate.now());

        Expense updatedExpense = expenseRepository.save(expense);
        return mapper.map(updatedExpense, ExpenseDto.class);
    }

    public void delete(Long id){
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));

        expenseRepository.delete(expense);

    }
}
