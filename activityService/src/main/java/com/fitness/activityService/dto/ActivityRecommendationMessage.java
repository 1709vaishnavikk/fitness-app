package com.fitness.activityService.dto;

import com.fitness.activityService.model.Activity;
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