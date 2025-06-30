package com.fitness.userService.service;

import com.fitness.userService.dto.RegisterRequest;
import com.fitness.userService.dto.UserProfileUpdateRequest;
import com.fitness.userService.dto.UserResponse;
import com.fitness.userService.model.User;
import com.fitness.userService.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserResponse register(RegisterRequest request) {
        log.info("Attempting to register user with email: {} and keycloakId: {}", request.getEmail(), request.getKeycloakId());

        if (repository.existsByEmail(request.getEmail())) {
            User existingUser = repository.findByEmail(request.getEmail());
            log.warn("User with email {} already exists. Returning existing user.", request.getEmail());
            return mapToResponse(existingUser);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setKeycloakId(request.getKeycloakId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        // --- NEW FIELDS ASSIGNMENT (from initial registration) ---
        user.setHeightCm(request.getHeightCm());
        user.setWeightKg(request.getWeightKg());
        user.setAge(request.getAge());
        user.setGoalWeightKg(request.getGoalWeightKg());
        user.setDailyWaterIntakeLiters(request.getDailyWaterIntakeLiters());
        user.setDietType(request.getDietType());
        user.setProfileCompleted(true); // Assuming full profile is provided at registration
        // --- END NEW FIELDS ASSIGNMENT ---

        try {
            User savedUser = repository.save(user);
            log.info("✅ New user saved successfully for keycloakId: {}", user.getKeycloakId());
            return mapToResponse(savedUser);
        } catch (Exception e) {
            log.error("❌ Error saving user: {}", e.getMessage(), e);
            throw new RuntimeException("Error during registration");
        }
    }


    // ❗ FIXED: You must query by keycloakId, not by ID
    public UserResponse getUserProfile(String keycloakId) {
        User user = repository.findByKeycloakId(keycloakId);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        return mapToResponse(user);
    }

    public UserResponse updateProfile(String keycloakId, @Valid UserProfileUpdateRequest request) {
        User user = repository.findByKeycloakId(keycloakId);
        if (user == null) {
            throw new RuntimeException("User Not Found for keycloakId: " + keycloakId);
        }

        user.setHeightCm(request.getHeightCm());
        user.setWeightKg(request.getWeightKg());
        user.setAge(request.getAge());
        user.setGoalWeightKg(request.getGoalWeightKg());
        user.setDailyWaterIntakeLiters(request.getDailyWaterIntakeLiters());
        user.setDietType(request.getDietType());
        user.setProfileCompleted(true); // Mark profile as completed after update

        try {
            User updatedUser = repository.save(user);
            log.info("✅ User profile updated successfully for keycloakId: {}", keycloakId);
            return mapToResponse(updatedUser);
        } catch (Exception e) {
            log.error("❌ Error updating user profile: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating user profile");
        }
    }
    // --- END NEW METHOD ---

    public Boolean existByUserId(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return repository.existsByKeycloakId(userId);
    }

    // ✅ Common mapper to convert User -> UserResponse
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setKeycloakId(user.getKeycloakId());
        response.setPassword(user.getPassword()); // Still considering if this should be here
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        // --- NEW FIELDS MAPPING ---
        response.setHeightCm(user.getHeightCm());
        response.setWeightKg(user.getWeightKg());
        response.setAge(user.getAge());
        response.setGoalWeightKg(user.getGoalWeightKg());
        response.setDailyWaterIntakeLiters(user.getDailyWaterIntakeLiters());
        response.setDietType(user.getDietType());
        response.setProfileCompleted(user.isProfileCompleted());
        // --- END NEW FIELDS MAPPING ---

        return response;
    }
}
