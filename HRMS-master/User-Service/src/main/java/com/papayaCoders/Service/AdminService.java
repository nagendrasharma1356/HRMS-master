package com.papayaCoders.Service;

import com.papayaCoders.Dto.AdminDto;
import com.papayaCoders.Repository.AdminRepository;
import com.papayaCoders.Utils.PasswordUtils;
import com.papayaCoders.common.ApiResponse;
import com.papayaCoders.exception.ResourceNotFoundException;
import com.papayaCoders.model.Admin;
import com.papayaCoders.model.Hr;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.papayaCoders.Utils.PasswordUtils.checkPassword;

@Service
public class AdminService
{
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper mapper;

    // create
    public ApiResponse<AdminDto> create(AdminDto adminDto){
        Admin map = mapper.map(adminDto, Admin.class);
        map.setPassword(PasswordUtils.hashPassword(adminDto.getPassword()));
        map.setCreatedAt(LocalDate.now());
        map.setUpdatedAt(LocalDate.now());
        Admin save = adminRepository.save(map);
        AdminDto map1 = mapper.map(save, AdminDto.class);
        return new ApiResponse<>(true,"Admin created",map1);
    }
    public ApiResponse delete(Long id){
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        adminRepository.delete(admin);
        return new ApiResponse(true, "admin deleted");
    }

    public Admin validateUser(String email, String password){
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            Admin user = admin.get();
            // Check if the provided password matches the stored hashed password
            if (checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
