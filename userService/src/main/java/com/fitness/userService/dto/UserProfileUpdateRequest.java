package com.fitness.userService.dto;

import com.fitness.userService.model.DietType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    @NotNull(message = "Height is required")
    @Min(value = 50, message = "Height must be at least 50 cm")
    private Double heightCm;

    @NotNull(message = "Weight is required")
    @Min(value = 20, message = "Weight must be at least 20 kg")
    private Double weightKg;

    @NotNull(message = "Age is required")
    @Min(value = 12, message = "Age must be at least 12")
    private Integer age;

    @NotNull(message = "Goal weight is required")
    @Min(value = 10, message = "Goal weight must be at least 10 kg")
    private Double goalWeightKg;

    @NotNull(message = "Daily water intake is required")
    @Min(value = 0, message = "Water intake cannot be negative")
    private Double dailyWaterIntakeLiters;

    @NotNull(message = "Diet type is required")
    private DietType dietType;
}