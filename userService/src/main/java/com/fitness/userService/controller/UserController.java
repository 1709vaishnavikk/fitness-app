package com.fitness.userService.controller;

import com.fitness.userService.dto.RegisterRequest;
import com.fitness.userService.dto.UserProfileUpdateRequest;
import com.fitness.userService.dto.UserResponse;
import com.fitness.userService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Get user by keycloakId
    @GetMapping("/{keycloakId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String keycloakId) {
        return ResponseEntity.ok(userService.getUserProfile(keycloakId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("✅ Received register request for email: " + request.getEmail());
        return ResponseEntity.ok(userService.register(request));
    }

    // ✅ Validation endpoint
    @GetMapping("/validate/{keycloakId}")
    public ResponseEntity<Boolean> validateUser(@PathVariable String keycloakId) {
        Boolean exists = userService.existByUserId(keycloakId);
        return ResponseEntity.ok(exists);
    }
    // --- NEW ENDPOINT: Update User Profile ---
    @PutMapping("/{keycloakId}/profile")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable String keycloakId,
                                                      @Valid @RequestBody UserProfileUpdateRequest request) {
        System.out.println("✅ Received profile update request for keycloakId: " + keycloakId);
        return ResponseEntity.ok(userService.updateProfile(keycloakId, request));
    }
    // --- END NEW ENDPOINT ---

    @GetMapping("/test")
    public ResponseEntity<String> testProtected() {
        return ResponseEntity.ok("Protected route reached!");
    }

}
