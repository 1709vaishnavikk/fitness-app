package com.fitness.userService.dto;


import com.fitness.userService.model.DietType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private String id;


    private String email;



    private String password;
    private String keycloakId;
    private String firstName;
    private String lastName;
    // --- NEW FIELDS START ---
    private Double heightCm;
    private Double weightKg;
    private Integer age;
    private Double goalWeightKg;
    private Double dailyWaterIntakeLiters;
    private DietType dietType;
    private boolean profileCompleted;



    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
}
