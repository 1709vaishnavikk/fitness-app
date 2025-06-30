package com.fitness.gateway.user;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private String id; // Internal database ID of the user in UserService
    private String email;
    // private String password; // Should generally not be exposed/included in responses
    private String keycloakId; // The ID from Keycloak
    private String firstName;
    private String lastName;

    // --- NEW FIELDS (Must mirror UserService's UserResponse exactly) ---
    private Double heightCm;
    private Double weightKg;
    private Integer age;
    private Double goalWeightKg;
    private Double dailyWaterIntakeLiters;
    private DietType dietType; // Use the DietType enum here
    private boolean profileCompleted; // Crucial flag for frontend flow
    // --- END NEW FIELDS ---

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}