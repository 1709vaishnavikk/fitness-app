package com.fitness.aiService.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Activity {

    private String userId;
    private String id;
    private String type;
//    private String activityId;

    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
