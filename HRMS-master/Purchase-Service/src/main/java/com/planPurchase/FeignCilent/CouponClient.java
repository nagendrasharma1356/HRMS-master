package com.planPurchase.FeignCilent;

import com.planPurchase.Common.ApiResponse;
import com.planPurchase.FeignClientDto.CouponDto;
import com.planPurchase.Interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COUPON-SERVICE",url = "${coupon.url}",  configuration = FeignClientInterceptor.class)
public interface CouponClient
{

    @GetMapping("/api/coupons/{id}")
    ApiResponse<CouponDto> getCouponById(@PathVariable("id") Long id);
}
