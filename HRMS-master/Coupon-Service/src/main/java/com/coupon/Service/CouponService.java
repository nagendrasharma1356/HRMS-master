package com.coupon.Service;

import com.coupon.Dto.CouponDto;
import com.coupon.Entity.Coupon;
import com.coupon.ExceptionHandling.ResourceAlreadyExistsException;
import com.coupon.Repository.CouponRepository;
import com.coupon.common.PagedResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CouponService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CouponRepository couponRepository;

    // Create Coupon
    public CouponDto createCoupon(CouponDto couponDto) {

        //  Check if same couponName already exists
        if (couponRepository.findByCouponName(couponDto.getCouponName()) != null) {
            throw new ResourceAlreadyExistsException("Coupon with name '" + couponDto.getCouponName() + "' already exists.");
        }
        //  If couponCode not provided, generate unique one
        if (couponDto.getCouponCode() == null || couponDto.getCouponCode().isBlank()) {
            String generatedCode;
            do {
                generatedCode = generateUniqueCouponCode();
            } while (couponRepository.findByCouponCode(generatedCode) != null);

            couponDto.setCouponCode(generatedCode);
        }

        couponDto.setCreatedAt(LocalDate.now());
        couponDto.setUpdatedAt(LocalDate.now());

        Coupon coupon = this.mapper.map(couponDto, Coupon.class);
        Coupon saved = couponRepository.save(coupon);
        return this.mapper.map(saved, CouponDto.class);
    }


    //  Update Coupon
    public CouponDto updateCoupon(Long id, CouponDto couponDto) {
        Coupon existing = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with ID: " + id));

        couponDto.setId(existing.getId());
        couponDto.setCreatedAt(existing.getCreatedAt());  // Preserve original creation date
        couponDto.setUpdatedAt(LocalDate.now());

        Coupon updatedCoupon = this.mapper.map(couponDto, Coupon.class);
        Coupon saved = couponRepository.save(updatedCoupon);
        return this.mapper.map(saved, CouponDto.class);
    }

    //  Get All Coupons
    public PagedResponse<CouponDto> getAllCoupons(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Coupon> pageCoupons = couponRepository.findAll(pageable);

        List<CouponDto> content = pageCoupons.getContent()
                .stream()
                .map(coupon -> mapper.map(coupon, CouponDto.class))
                .collect(Collectors.toList());

        PagedResponse<CouponDto> response = PagedResponse.<CouponDto>builder()
                .content(content)
                .pageNumber(pageCoupons.getNumber())
                .pageSize(pageCoupons.getSize())
                .totalElements(pageCoupons.getTotalElements())
                .totalPages(pageCoupons.getTotalPages())
                .lastPage(pageCoupons.isLast())
                .build();

        return response;
    }
    //  Get Coupon by ID
    public CouponDto getCouponById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with ID: " + id));
        return this.mapper.map(coupon, CouponDto.class);
    }

    //  Delete Coupon
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with ID: " + id));
        couponRepository.delete(coupon);
    }
    private String generateUniqueCouponCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

}
