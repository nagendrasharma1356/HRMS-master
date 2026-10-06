package com.papayaCoders.FeignClientService;

import com.papayaCoders.Dto.PermissionDto;
import com.papayaCoders.Interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ROLE-AND-PERMISSION-SERVICE",url = "${base.url}",configuration = FeignClientInterceptor.class) // Replace with actual host & port of your role-service
public interface RoleServiceClient {

    @GetMapping("/api/public/{roleName}/permissions")
    List<PermissionDto> getPermissionsByRole(@PathVariable("roleName") String roleName);


}