package com.expense.repository;

import com.expense.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory,Long> {
    boolean existsByName(String name);

}
