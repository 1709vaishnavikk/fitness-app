package com.fitness.userService.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String keycloakId;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role=UserRole.USER;

    // --- NEW FIELDS START ---
    private Double heightCm; // Height in centimeters
    private Double weightKg; // Weight in kilograms
    private Integer age;
    private Double goalWeightKg; // Goal weight in kilograms
    private Double dailyWaterIntakeLiters; // Amount of water drunk daily in liters

    @Enumerated(EnumType.STRING)
    private DietType dietType; // Enum for diet type

    @Column(nullable = false, columnDefinition = "boolean default false") // <--- ADD THIS
    private boolean profileCompleted;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
