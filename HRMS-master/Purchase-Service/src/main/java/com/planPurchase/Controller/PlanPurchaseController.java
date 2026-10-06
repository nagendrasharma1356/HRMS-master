package com.planPurchase.Controller;

import com.planPurchase.Common.ApiResponse;
import com.planPurchase.Common.PageResponse;
import com.planPurchase.Dto.PlanPurchaseDto;
import com.planPurchase.Service.PlanPurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planPurchase")
@Tag(name = "Plan Purchase API", description = "Handles operations related to purchasing and managing plans.")
public class PlanPurchaseController {

    @Autowired
    private PlanPurchaseService planPurchaseService;

    @Operation(summary = "Purchase a Plan", description = "Creates a new purchase record for the selected plan.")
    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<PlanPurchaseDto>> purchasePlan(@RequestBody PlanPurchaseDto dto) {
        PlanPurchaseDto savedPurchase = planPurchaseService.purchase(dto);
        ApiResponse<PlanPurchaseDto> response = new ApiResponse<>(
                true,
                "Plan purchased successfully",
                savedPurchase
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Plan Purchase by ID", description = "Fetches purchase details using the purchase ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlanPurchaseDto>> getPlanById(@PathVariable Long id) {
        PlanPurchaseDto dto = planPurchaseService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Plan purchase fetched successfully", dto));
    }

    @Operation(summary = "Get All Plan Purchases with Pagination", description = "Returns a paginated list of all plan purchases.")
    @GetMapping("all")
    public ResponseEntity<ApiResponse<PageResponse<PlanPurchaseDto>>> getAllPlans(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        PageResponse<PlanPurchaseDto> response = planPurchaseService.getAll(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Plan purchases fetched successfully", response));
    }


    @PutMapping("/payment/{purchaseId}")
    public ResponseEntity<ApiResponse<PlanPurchaseDto>> markPaymentSuccess(
            @PathVariable Long purchaseId,
            @RequestParam String razorpayPaymentId) {

        PlanPurchaseDto updated = planPurchaseService.markPaymentSuccess(purchaseId, razorpayPaymentId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment marked as Success", updated));
    }

}
