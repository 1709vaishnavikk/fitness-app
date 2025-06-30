package com.fitness.aiService.service;

import com.fitness.aiService.model.Recommendation;
import com.fitness.aiService.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j

public class RecommendationService {

    @Autowired
    private final RecommendationRepository repository;

    public RecommendationService(RecommendationRepository repository) {
        this.repository = repository;
    }

    public List<Recommendation> getUserRecommendation(String userId) {
        return repository.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        log.info("Fetching recommendation for activityId={}", activityId);

        return repository.findByActivityId(activityId)
                .orElseThrow(()-> new RuntimeException("No recommendation found!"));
    }
}
