package com.papaya.notice.FeignClientService;




import com.papaya.notice.Interceptor.FeignClientInterceptor;
import com.papaya.notice.dto.PermissionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ROLE-AND-PERMISSION-SERVICE", url = "${base.url}",configuration = FeignClientInterceptor.class)
public interface RoleServiceClient {

    @GetMapping("/api/public/{roleName}/permissions")
    List<PermissionDto> getPermissionsByRole(@PathVariable("roleName") String roleName);


}
