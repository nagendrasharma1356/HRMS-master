package com.leave_management.service;

import com.leave_management.Entity.Leave;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.Month;
public class LeaveSpecification {

    public static Specification<Leave> filterBySessionMonthYear(String session, String month, Integer year) {
        return (root, query, cb) -> {
            LocalDate today = LocalDate.now();

            // Session filter
            Predicate sessionPredicate;
            if ("today".equalsIgnoreCase(session)) {
                sessionPredicate = cb.equal(root.get("leaveDate"), today);
            } else if ("tomorrow".equalsIgnoreCase(session)) {
                sessionPredicate = cb.equal(root.get("leaveDate"), today.plusDays(1));
            } else if ("upcoming".equalsIgnoreCase(session)) {
                sessionPredicate = cb.greaterThan(root.get("leaveDate"), today);
            } else {
                sessionPredicate = cb.conjunction();
            }


            Predicate monthPredicate;
            if (month != null && !month.isEmpty()) {
                try {
                    Month monthEnum = Month.valueOf(month.toUpperCase());
                    Expression<Integer> monthExpression = cb.function("month", Integer.class, root.get("leaveDate"));
                    monthPredicate = cb.equal(monthExpression, monthEnum.getValue());
                } catch (IllegalArgumentException e) {
                    monthPredicate = cb.conjunction();
                }
            } else {
                monthPredicate = cb.conjunction();
            }


            Predicate yearPredicate;
            if (year != null) {
                Expression<Integer> yearExpression = cb.function("year", Integer.class, root.get("leaveDate"));
                yearPredicate = cb.equal(yearExpression, year);
            } else {
                yearPredicate = cb.conjunction();
            }

            return cb.and(sessionPredicate, monthPredicate, yearPredicate);
        };
    }
}
