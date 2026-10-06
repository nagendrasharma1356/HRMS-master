package com.planPurchase.Repository;

import com.planPurchase.Entity.PlanPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanPurchaseRepository extends JpaRepository<PlanPurchase, Long> {

    boolean existsByClientIdAndPlanId(Long clientId, Long planId);
}
