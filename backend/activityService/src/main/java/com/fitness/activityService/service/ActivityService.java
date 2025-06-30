package com.fitness.activityService.service;

import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;
import com.fitness.activityService.dto.UserResponse; // The UPDATED UserResponse DTO
import com.fitness.activityService.dto.ActivityRecommendationMessage; // Import the NEW DTO
import com.fitness.activityService.model.Activity;
import com.fitness.activityService.repository.ActivityRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final RabbitTemplate rabbitTemplate;
    private final UserClient userClient; // This is correctly injected

    @Autowired
    public ActivityService(ActivityRepository activityRepository, RabbitTemplate rabbitTemplate, UserClient userClient) {
        this.activityRepository = activityRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.userClient = userClient;
    }

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    public ActivityResponse trackActivity(ActivityRequest request){
        // --- CHANGE 1: Fetch User Profile using Feign Client ---
        UserResponse userProfile;
        try {
            // Use getUserById which now correctly fetches the full UserResponse by keycloakId
            userProfile = userClient.getUserById(request.getUserId());
            log.info("✅ Successfully fetched user profile for Keycloak ID: {}", request.getUserId());

            // Optional: Check if profile is completed (as set in UserService)
            if (userProfile.isProfileCompleted() == false) { // Assuming false for incomplete
                log.warn("User profile is not marked as completed for Keycloak ID: {}. AI recommendations might be less accurate.", request.getUserId());
                // You could throw an exception here if you want to enforce profile completion
                // throw new RuntimeException("Please complete your profile before logging activities.");
            }

        } catch (FeignException.NotFound e) {
            log.error("❌ User not found in UserService for Keycloak ID: {}", request.getUserId(), e);
            throw new RuntimeException("User not found with id: " + request.getUserId() + ". Please ensure user exists and profile is setup.");
        } catch (Exception e) {
            log.error("❌ Error fetching user profile from UserService for Keycloak ID {}: {}", request.getUserId(), e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user profile: " + e.getMessage());
        }


        // --- No Change: Build Activity Object ---
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        log.info("✅ Activity saved successfully with ID: {}", savedActivity.getId());


        // --- CHANGE 2: Publish Comprehensive Message to RabbitMQ ---
        // Create the new message DTO containing both activity and user profile
        ActivityRecommendationMessage message = new ActivityRecommendationMessage(savedActivity, userProfile);

        try{
            // Send the new combined message DTO
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("✅ Activity and user profile published to RabbitMQ for AI recommendation. Message: {}", message);

        }catch(Exception e){
            log.error("❌ Failed to publish activity and user profile in RabbitMQ for Keycloak ID {}: {}", request.getUserId(), e.getMessage(), e);
            // Decide on error handling: throw, retry, log, etc.
        }

        return mapToResponse(savedActivity) ;
    }

    // This method remains unchanged as it maps Activity to ActivityResponse
    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response=new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setStartTime(activity.getStartTime());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;
    }

    // This method remains unchanged
    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities=activityRepository.findByUserId(userId);
        return activities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // This method remains unchanged
    public ActivityResponse getActivityById(String activityId) {
        return activityRepository.findById(activityId)
                .map(this::mapToResponse)
                .orElseThrow(()->new RuntimeException("Activity not found with id" + activityId));
    }
}