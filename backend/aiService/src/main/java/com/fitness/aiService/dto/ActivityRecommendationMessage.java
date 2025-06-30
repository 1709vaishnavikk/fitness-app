package com.fitness.aiService.dto;

import com.fitness.aiService.model.Activity; // Import the existing Activity model in AIService
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRecommendationMessage {
    private Activity activity; // The activity that was just tracked
    private UserResponse userProfile; // The complete user profile fetched from UserService
}