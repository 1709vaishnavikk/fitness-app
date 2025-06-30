package com.fitness.gateway.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6,message = "Password must have atleast 6 characters")
    private String password; // Will be a dummy password for Keycloak-synced users
    private String keycloakId;
    private String firstName;
    private String lastName;

    // --- NEW FIELDS: Add these to mirror UserService's RegisterRequest (they'll be null initially for sync) ---
    private Double heightCm;
    private Double weightKg;
    private Integer age;
    private Double goalWeightKg;
    private Double dailyWaterIntakeLiters;
    private String dietType; // Use String here to match how you likely pass it from frontend
    // --- END NEW FIELDS ---
}