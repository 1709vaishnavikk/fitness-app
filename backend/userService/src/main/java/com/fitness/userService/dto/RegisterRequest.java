package com.fitness.userService.dto;

import com.fitness.userService.model.DietType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6,message = "Password must have atleast 6 characters")
    private String password;
    private String keycloakId;
    private String firstName;
    private String lastName;

    // --- NEW FIELDS START ---
    @NotNull(message = "Height is required")
    @Min(value = 50, message = "Height must be at least 50 cm") // Example min height
    private Double heightCm;

    @NotNull(message = "Weight is required")
    @Min(value = 10, message = "Weight must be at least 10 kg") // Example min weight
    private Double weightKg;

    @NotNull(message = "Age is required")
    @Min(value = 12, message = "Age must be at least 12")
    private Integer age;

    @NotNull(message = "Goal weight is required")
    @Min(value = 20, message = "Goal weight must be at least 20 kg")
    private Double goalWeightKg;

    @NotNull(message = "Daily water intake is required")
    @Min(value = 0, message = "Water intake cannot be negative")
    private Double dailyWaterIntakeLiters;

    @NotNull(message = "Diet type is required")
    private DietType dietType; // Use the new enum

}
