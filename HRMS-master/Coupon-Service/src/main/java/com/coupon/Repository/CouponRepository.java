package com.coupon.Repository;

import com.coupon.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long>{
    Coupon findByCouponName(String couponName);

    Coupon findByCouponCode(String couponCode);

}
