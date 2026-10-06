package com.coupon.Controller;

import com.coupon.Dto.CouponDto;
import com.coupon.Service.CouponService;
import com.coupon.common.ApiResponse;
import com.coupon.common.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@Tag(name = "Coupon API", description = "Operations related to Coupons management")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Operation(summary = "Create a new Coupon", description = "Adds a new coupon to the system.")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<CouponDto>> createCoupon(@RequestBody CouponDto couponDto) {
        CouponDto created = couponService.createCoupon(couponDto);
        ApiResponse<CouponDto> response = new ApiResponse<>(true, "Coupon created successfully", created);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update existing Coupon", description = "Updates coupon details based on provided ID.")
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<CouponDto>> updateCoupon(@PathVariable Long id, @RequestBody CouponDto couponDto) {
        CouponDto updated = couponService.updateCoupon(id, couponDto);
        ApiResponse<CouponDto> response = new ApiResponse<>(true, "Coupon updated successfully", updated);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Coupon by ID", description = "Fetches coupon details using coupon ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponDto>> getCouponById(@PathVariable Long id) {
        CouponDto coupon = couponService.getCouponById(id);
        ApiResponse<CouponDto> response = new ApiResponse<>(true, "Coupon fetched successfully", coupon);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all Coupons with pagination", description = "Fetches a paginated list of all coupons.")
    @GetMapping("all")
    public ResponseEntity<ApiResponse<PagedResponse<CouponDto>>> getAllCoupons(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PagedResponse<CouponDto> responseData = couponService.getAllCoupons(pageNumber, pageSize, sortBy, sortDir);
        ApiResponse<PagedResponse<CouponDto>> response = new ApiResponse<>(true, "Coupons fetched successfully", responseData);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete Coupon", description = "Deletes a coupon from the system by ID.")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        ApiResponse<String> response = new ApiResponse<>(true, "Coupon deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
