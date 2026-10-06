package com.planPurchase.Service;

import com.planPurchase.Common.ApiResponse;
import com.planPurchase.Common.PageResponse;
import com.planPurchase.Dto.PlanPurchaseDto;
import com.planPurchase.Entity.PlanPurchase;
import com.planPurchase.Enum.PaymentStatus;
import com.planPurchase.Enum.PlanStatus;
import com.planPurchase.FeignCilent.CompanyClient;
import com.planPurchase.FeignCilent.CompanyServiceClient;
import com.planPurchase.FeignCilent.CouponClient;
import com.planPurchase.FeignClientDto.CompanyDto;
import com.planPurchase.FeignClientDto.CompanyServiceDto;
import com.planPurchase.FeignClientDto.CouponDto;
import com.planPurchase.Repository.PlanPurchaseRepository;
import com.planPurchase.exception.ResourceAlreadyExistsException;
import com.planPurchase.exception.ResourceNotFoundException;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlanPurchaseService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PlanPurchaseRepository purchaseRepository;

    @Autowired
    private CompanyServiceClient companyServiceClient;

    @Autowired
    private CouponClient couponClient;
    @Autowired
    private CompanyClient companyClient;
    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;


    public PlanPurchaseDto purchase(PlanPurchaseDto dto) {

        // Step 1: Validate Company
        CompanyDto companyDto;
        try {
            companyDto = companyClient.getCompanyById(dto.getCompanyId());
        } catch (Exception e) {
            throw new RuntimeException("Invalid Company ID: " + e.getMessage());
        }

        // Step 2: Prevent Duplicate Purchase
        boolean exists = purchaseRepository.existsByClientIdAndPlanId(dto.getClientId(), dto.getPlanId());
        if (exists) {
            throw new ResourceAlreadyExistsException("Plan already purchased by this client.");
        }
        PlanPurchase planPurchase = new PlanPurchase();

        // Step 3: Fetch Plan/Service Details
        ApiResponse<CompanyServiceDto> response = companyServiceClient.getServiceById(dto.getPlanId());
        if (response == null || response.getData() == null) {
            throw new RuntimeException("Invalid Service Response. Data is missing.");
        }
        CompanyServiceDto serviceDto = response.getData();

        // Validate Plan belongs to provided Company
        if (!dto.getCompanyId().equals(Long.valueOf(serviceDto.getCompanyId()))) {
            throw new RuntimeException("Plan does not belong to the provided company.");
        }

        if (serviceDto.getFinalPrice() == null) {
            throw new RuntimeException("Final price is missing for the service.");
        }

        double finalPrice = serviceDto.getFinalPrice();
        double companyDiscount= serviceDto.getPrice()- serviceDto.getFinalPrice();

        if (dto.getCouponId() != null) {
            try {
                ApiResponse<CouponDto> couponResponse = couponClient.getCouponById(dto.getCouponId());

                if (couponResponse == null || couponResponse.getData() == null) {
                    throw new RuntimeException("Invalid Coupon Response.");
                }

                CouponDto coupon = couponResponse.getData();

                if (coupon.getValidTill() != null && LocalDate.parse(coupon.getValidTill()).isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("Coupon is expired.");
                }

                if (coupon.getDiscount() != null && coupon.getDiscount() > 0) {
                    Double couponDiscount = finalPrice * coupon.getDiscount() / 100.0;
                    finalPrice = finalPrice - couponDiscount;

                    planPurchase.setCouponId(coupon.getId());
                    planPurchase.setCouponDiscount(couponDiscount);
                }

            } catch (Exception e) {
                throw new RuntimeException("Invalid Coupon: " + e.getMessage());
            }
        }





        // Step 6: Set Final Data to Entity
        planPurchase.setClientId(dto.getClientId());
        planPurchase.setCompanyId(dto.getCompanyId());
        planPurchase.setPlanId(dto.getPlanId());
        planPurchase.setTransactionId(dto.getTransactionId());
        planPurchase.setPaymentStatus(PaymentStatus.PENDING);
        planPurchase.setPlanStatus(PlanStatus.PENDING);
        planPurchase.setCreatedAt(LocalDateTime.now());
        planPurchase.setUpdatedAt(LocalDateTime.now());
        planPurchase.setCouponId(dto.getCouponId());
        planPurchase.setPaymentMode(dto.getPaymentMode());
        planPurchase.setRemarks(dto.getRemarks());
        planPurchase.setCompanyDiscount(companyDiscount);
        planPurchase.setTotalAmount(finalPrice);
        // CouponId set above if applied

        // Save to DB
        planPurchase = purchaseRepository.save(planPurchase);

                PlanPurchaseDto responseDto = mapper.map(planPurchase, PlanPurchaseDto.class);
        responseDto.setCouponId(planPurchase.getCouponId());

        return responseDto;
    }
    public PlanPurchaseDto markPaymentSuccess(Long purchaseId, String razorpayPaymentId) {
        PlanPurchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found"));

        if (purchase.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment already marked as SUCCESS.");
        }

        purchase.setPaymentStatus(PaymentStatus.SUCCESS);
        purchase.setPlanStatus(PlanStatus.RUNNING);
        purchase.setTransactionId(razorpayPaymentId);
        purchase.setUpdatedAt(LocalDateTime.now());

        purchase = purchaseRepository.save(purchase);

        return mapper.map(purchase, PlanPurchaseDto.class);
    }


    private String createRazorpayOrder(Double amount, String receipt) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount.intValue() * 100);  // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receipt);
            orderRequest.put("payment_capture", true);

            Order order = razorpay.orders.create(orderRequest);

            return order.get("id");
        } catch (Exception e) {
            throw new RuntimeException("Razorpay Order creation failed: " + e.getMessage());
        }
    }













    public PlanPurchaseDto getById(Long id) {
        PlanPurchase planPurchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan purchase not found with ID: " + id));
        return mapper.map(planPurchase, PlanPurchaseDto.class);
    }

    public PageResponse<PlanPurchaseDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PlanPurchase> planPage = purchaseRepository.findAll(pageable);

        List<PlanPurchaseDto> content = planPage.getContent()
                .stream()
                .map(plan -> mapper.map(plan, PlanPurchaseDto.class))
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                planPage.getNumber(),
                planPage.getSize(),
                planPage.getTotalElements(),
                planPage.getTotalPages(),
                planPage.isLast()
        );
    }

}
