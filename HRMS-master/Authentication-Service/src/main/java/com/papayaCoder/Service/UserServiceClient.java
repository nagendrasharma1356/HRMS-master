package com.papayaCoder.Service;

import com.papayaCoder.Dto.LoginRequest;

import com.papayaCoder.Dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name = "user-service", url = "${base.url}")  // Change to actual URL of user-service
public interface UserServiceClient {


    @PostMapping("/api/hr/validate")
    ResponseEntity<UserDto> validateHr(@RequestBody LoginRequest loginRequest);

     @PostMapping("/api/admin/validate")
    ResponseEntity<UserDto> validateAdmin(@RequestBody LoginRequest loginRequest);

     @PostMapping("/api/employee/validate")
    ResponseEntity<UserDto> validateEmployee(@RequestBody LoginRequest loginRequest);

     @PostMapping("/api/manager/validate")
    ResponseEntity<UserDto> validateManager(@RequestBody LoginRequest loginRequest);


}
