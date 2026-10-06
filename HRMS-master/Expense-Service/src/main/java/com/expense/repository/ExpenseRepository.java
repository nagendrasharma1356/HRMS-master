package com.expense.repository;

import com.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense,Long>
{
    boolean existsByTitle(String title);

}
