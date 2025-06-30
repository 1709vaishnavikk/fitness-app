package com.fitness.aiService.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recommendations")
@Data
@Builder
public class Recommendation {
    @Id
    private String id;
    private String activityId;
    private String userId; // Keycloak ID
    private String activityType;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;

    // --- NEW FIELDS: Snapshot of user profile data at the time of recommendation ---
    private Double userHeightCm;
    private Double userWeightKg;
    private Integer userAge;
    private Double userGoalWeightKg;
    private Double userDailyWaterIntakeLiters;
    private DietType userDietType; // Make sure DietType enum is accessible
    // --- END NEW FIELDS ---

    @CreatedDate
    private LocalDateTime createdAt;
}