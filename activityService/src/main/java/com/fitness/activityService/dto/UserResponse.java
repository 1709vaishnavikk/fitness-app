package com.fitness.activityService.dto;

import com.fitness.activityService.model.DietType;
import lombok.Data;



@Data
public class UserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    // --- NEW FIELDS START (Mirroring UserService's UserResponse DTO) ---
    private Double heightCm;
    private Double weightKg;
    private Integer age;
    private Double goalWeightKg;
    private Double dailyWaterIntakeLiters;
    private DietType dietType; // Use the DietType enum
    private boolean profileCompleted; // Flag from UserService
    // --- NEW FIELDS END ---
}

