package com.fitness.aiService.service;

import com.fitness.aiService.dto.ActivityRecommendationMessage; // Import the NEW DTO
import com.fitness.aiService.model.Recommendation;
import com.fitness.aiService.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private final ActivityAIService aiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues ="activity.queue" )
    @RabbitListener(queues = "activity.queue")
    public void processActivity(ActivityRecommendationMessage message) {
        log.info("📩 Received message in AI Service: {}", message);

        try {
            Recommendation recommendation = aiService.generateRecommendation(message.getActivity(), message.getUserProfile());
            log.info("✅ Generated Recommendation: {}", recommendation);
            recommendationRepository.save(recommendation);
            log.info("📥 Recommendation saved with ID: {}", recommendation.getId());
        } catch (Exception e) {
            log.error("❌ Error in processing AI recommendation: ", e);
        }
    }
}