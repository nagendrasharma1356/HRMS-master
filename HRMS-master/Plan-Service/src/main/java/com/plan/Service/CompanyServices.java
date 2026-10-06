package com.plan.Service;

import com.plan.Common.PageResponse;
import com.plan.Dto.CompanyServiceDto;
import com.plan.Entity.CompanyService;
import com.plan.FeignClient.CompanyClient;
import com.plan.Repository.CompanyServiceRepository;
import com.plan.exception.ResourceAlreadyExistsException;
import com.plan.exception.ResourceNotFoundException;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServices
{
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CompanyClient companyClient;

    @Autowired
    private CompanyServiceRepository companyServiceRepository;
    public CompanyServiceDto createService(CompanyServiceDto dto) {

        try {
            companyClient.getCompanyById(Long.valueOf(dto.getCompanyId()));
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Company with ID " + dto.getCompanyId() + " does not exist.");
        } catch (FeignException e) {
            throw new RuntimeException("Error while verifying company existence: " + e.getMessage());
        }

        // Step 2: Check Duplicate Service for this Company
        boolean exists = companyServiceRepository.existsByServiceNameAndCompanyId(dto.getServiceName(), Long.valueOf(dto.getCompanyId()));
        if (exists) {
            throw new ResourceAlreadyExistsException("Service with name '" + dto.getServiceName() + "' already exists for this company.");
        }

        CompanyService service = mapper.map(dto, CompanyService.class);

        double finalPrice = service.getPrice();
        if (service.getDiscount() != null && service.getDiscount() > 0) {
            finalPrice = finalPrice - (finalPrice * service.getDiscount() / 100.0);
        }
        service.setFinalPrice(finalPrice);
        service = companyServiceRepository.save(service);
        CompanyServiceDto responseDto = mapper.map(service, CompanyServiceDto.class);
        responseDto.setDiscount(service.getDiscount());  // Keep discount visible in response

        return responseDto;
    }



    public PageResponse<CompanyServiceDto> getAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CompanyService> servicePage = companyServiceRepository.findAll(pageable);

        List<CompanyServiceDto> content = servicePage.getContent()
                .stream()
                .map(service -> mapper.map(service, CompanyServiceDto.class))
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                servicePage.getNumber(),
                servicePage.getSize(),
                servicePage.getTotalElements(),
                servicePage.getTotalPages(),
                servicePage.isLast()
        );
    }

    // Get Service by ID
    public CompanyServiceDto getServiceById(Long id) {
        CompanyService service = companyServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + id));
        return mapper.map(service, CompanyServiceDto.class);
    }

    // Update Service
    public CompanyServiceDto updateService(Long id, CompanyServiceDto dto) {
        CompanyService existing = companyServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + id));

        existing.setServiceName(dto.getServiceName());
        existing.setPrice(dto.getPrice());
        existing.setIsActive(dto.getIsActive());
        existing.setCompanyId(dto.getCompanyId());
        existing.setUpdatedAt(java.time.LocalDateTime.now());

        existing = companyServiceRepository.save(existing);
        return mapper.map(existing, CompanyServiceDto.class);
    }

    // Delete Service
    public void deleteService(Long id) {
        CompanyService service = companyServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + id));
        companyServiceRepository.delete(service);
    }
}
