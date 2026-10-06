package com.leave_management.FeignClientService;


import com.leave_management.Dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE", url = "${user.url}")
public interface UserClient {

    @GetMapping("/api/common/users/{role}/{id}")
    UserDto getUserByIdAndRole(@PathVariable("role") String role, @PathVariable("id") Long id);
}
