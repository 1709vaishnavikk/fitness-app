package com.fitness.activityService.service;


import com.fitness.activityService.config.FeignClientConfig;
import com.fitness.activityService.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userService", configuration = FeignClientConfig.class) // should match spring.application.name of user service
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    UserResponse getUserById(@PathVariable("userId") String userId);
}