package com.expense.service;

import com.expense.common.PagedResponse;
import com.expense.dto.ExpenseCategoryDto;
import com.expense.entity.ExpenseCategory;
import com.expense.repository.ExpenseCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseCategoryService {
    @Autowired
    private ExpenseCategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    // create
    public ExpenseCategoryDto create(ExpenseCategoryDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Category with name '" + dto.getName() + "' already exists.");
        }
        ExpenseCategory category = mapper.map(dto, ExpenseCategory.class);
        category.setCreatedAt(LocalDate.now());
        category.setUpdatedAt(LocalDate.now());
        ExpenseCategory expenseCategory = categoryRepository.save(category);
        return mapper.map(expenseCategory, ExpenseCategoryDto.class);
    }

    // getAll
    public PagedResponse<ExpenseCategoryDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExpenseCategory> categories = categoryRepository.findAll(pageable);
        List<ExpenseCategoryDto> collect = categories.stream()
                .map(expenseCategory -> mapper.map(expenseCategory, ExpenseCategoryDto.class))
                .collect(Collectors.toList());

        return PagedResponse.<ExpenseCategoryDto>builder()
                .content(collect)
                .pageNumber(categories.getNumber())
                .pageSize(categories.getSize())
                .totalElements(categories.getTotalElements())
                .last(categories.isLast())
                .build();
    }

    // getById
    public ExpenseCategoryDto getById(Long id){
        ExpenseCategory category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
        ExpenseCategoryDto map = mapper.map(category, ExpenseCategoryDto.class);
        return map;
    }

    // update
    public ExpenseCategoryDto update(Long id, ExpenseCategoryDto expenseCategoryDto){
        ExpenseCategory category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
        category.setName(expenseCategoryDto.getName());
        category.setDescription(expenseCategoryDto.getDescription());
        ExpenseCategory expenseCategory = categoryRepository.save(category);
        return mapper.map(expenseCategory,ExpenseCategoryDto.class);
    }

    // delete
    public void delete(Long id){
        ExpenseCategory category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
        categoryRepository.delete(category);

    }

}
