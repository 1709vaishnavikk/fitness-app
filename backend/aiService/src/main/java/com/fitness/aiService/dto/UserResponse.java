package com.fitness.aiService.dto;

import com.fitness.aiService.model.DietType; // Import the DietType enum we will create below
import lombok.Data;

// This DTO must mirror the UserResponse DTO in your UserService exactly
@Data
public class UserResponse {
    private String id; // This is the internal UUID ID of the User in UserService's DB
    private String email;
    // private String password; // It's good practice to NOT include sensitive info like password in DTOs if not strictly necessary
    private String keycloakId; // The ID from Keycloak, which is used for linking
    private String firstName;
    private String lastName;

    // --- NEW FIELDS (Mirroring UserService's UserResponse) ---
    private Double heightCm;
    private Double weightKg;
    private Integer age;
    private Double goalWeightKg;
    private Double dailyWaterIntakeLiters;
    private DietType dietType; // Use the DietType enum
    private boolean profileCompleted; // Flag from UserService
    // --- END NEW FIELDS ---
}