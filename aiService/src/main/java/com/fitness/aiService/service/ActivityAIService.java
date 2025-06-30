package com.fitness.aiService.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiService.dto.UserResponse; // Import the UserResponse DTO
import com.fitness.aiService.model.Activity;
import com.fitness.aiService.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {
    private final GeminiService geminiService;

    // --- CHANGE 1: Add UserResponse parameter to generateRecommendation ---
    public Recommendation generateRecommendation(Activity activity, UserResponse userProfile) {
        String prompt = createPromptForActivity(activity, userProfile); // Pass userProfile to prompt creation
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RAW RESPONSE FROM AI: {} ", aiResponse);

        return processAiResponse(activity, userProfile, aiResponse); // Pass userProfile here for optional saving in Recommendation model
    }

    // --- CHANGE 2: Add UserResponse parameter to processAiResponse for optional saving ---
    private Recommendation processAiResponse(Activity activity, UserResponse userProfile, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // --- FIX START ---
            // First, parse the entire AI response string into a JsonNode
            JsonNode rootNode = mapper.readTree(aiResponse);

            // Then, use the rootNode to navigate the JSON structure
            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");
            // --- FIX END ---


            if (textNode.isMissingNode() || !textNode.isTextual()) {
                log.error("AI response did not contain expected 'text' node or it's not textual: {}", aiResponse);
                return createDefaultRecommendation(activity, userProfile); // Pass userProfile to default as well
            }

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();

            log.info("PARSED JSON CONTENT FROM AI: {} ", jsonContent);

            JsonNode analysisJson = mapper.readTree(jsonContent);

            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    // --- OPTIONAL: Populate User Profile Snapshot in Recommendation ---
                    .userHeightCm(userProfile.getHeightCm())
                    .userWeightKg(userProfile.getWeightKg())
                    .userAge(userProfile.getAge())
                    .userGoalWeightKg(userProfile.getGoalWeightKg())
                    .userDailyWaterIntakeLiters(userProfile.getDailyWaterIntakeLiters())
                    .userDietType(userProfile.getDietType())
                    // --- END OPTIONAL POPULATION ---
                    .build();

        } catch (Exception e) {
            log.error("Error processing AI response for activity {}: {}", activity.getId(), e.getMessage());
            e.printStackTrace();
            return createDefaultRecommendation(activity, userProfile); // Pass userProfile to default as well
        }
    }

    // --- CHANGE 3: Add UserResponse parameter to createDefaultRecommendation ---
    private Recommendation createDefaultRecommendation(Activity activity, UserResponse userProfile) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed analysis. Please try again.")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                // --- OPTIONAL: Populate User Profile Snapshot in Default Recommendation ---
                .userHeightCm(userProfile.getHeightCm())
                .userWeightKg(userProfile.getWeightKg())
                .userAge(userProfile.getAge())
                .userGoalWeightKg(userProfile.getGoalWeightKg())
                .userDailyWaterIntakeLiters(userProfile.getDailyWaterIntakeLiters())
                .userDietType(userProfile.getDietType())
                // --- END OPTIONAL POPULATION ---
                .build();
    }

    // These extraction methods remain unchanged
    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        Set<String> uniqueSafety = new HashSet<>();
        if (safetyNode != null && safetyNode.isArray()) {
            for (JsonNode item : safetyNode) {
                if (item.isTextual() && !item.asText().trim().isEmpty()) {
                    uniqueSafety.add(item.asText().trim());
                }
            }
        }
        return uniqueSafety.isEmpty() ?
                Collections.singletonList("Follow general safety guidelines") :
                new ArrayList<>(uniqueSafety);
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        Set<String> uniqueSuggestions = new HashSet<>();
        if (suggestionsNode != null && suggestionsNode.isArray()) {
            for (JsonNode suggestion : suggestionsNode) {
                if (suggestion.isObject()) {
                    String workout = suggestion.path("workout").asText("").trim();
                    String description = suggestion.path("description").asText("").trim();
                    if (!workout.isEmpty() || !description.isEmpty()) {
                        String combined = String.format("%s: %s", workout, description).trim();
                        if (combined.contains(".\n") || combined.contains(". ")) {
                            String[] splitSuggestions = combined.split("\\.\\s*(?=[A-Z])");
                            for (String part : splitSuggestions) {
                                if (!part.trim().isEmpty()) {
                                    uniqueSuggestions.add(part.trim());
                                }
                            }
                        } else {
                            uniqueSuggestions.add(combined);
                        }
                    }
                }
            }
        }
        return uniqueSuggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions provided") :
                new ArrayList<>(uniqueSuggestions);
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        Set<String> uniqueImprovements = new HashSet<>();
        if (improvementsNode != null && improvementsNode.isArray()) {
            for (JsonNode improvement : improvementsNode) {
                if (improvement.isObject()) {
                    String area = improvement.path("area").asText("").trim();
                    String detail = improvement.path("recommendation").asText("").trim();

                    if (!area.isEmpty() || !detail.isEmpty()) {
                        String combined = String.format("%s: %s", area, detail).trim();

                        if (combined.contains(".\n") || combined.contains(". ") && combined.matches(".*\\.\\s*(Pace|Incline|Duration|Form|Strength|Endurance):.*")) {
                            String[] splitDetails = combined.split("\\.\\s*(?=(Pace|Incline|Duration|Form|Strength|Endurance|Recovery):)");
                            for (String part : splitDetails) {
                                if (!part.trim().isEmpty()) {
                                    uniqueImprovements.add(part.trim());
                                }
                            }
                        } else {
                            uniqueImprovements.add(combined);
                        }
                    }
                }
            }
        }
        return uniqueImprovements.isEmpty() ?
                Collections.singletonList("No specific improvements provided") :
                new ArrayList<>(uniqueImprovements);
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if (analysisNode != null && !analysisNode.path(key).isMissingNode() && analysisNode.path(key).isTextual()) {
            String text = analysisNode.path(key).asText().trim();
            if (!text.isEmpty()) {
                fullAnalysis.append(prefix)
                        .append(text)
                        .append("\n\n");
            }
        }
    }


    // --- CHANGE 4: Refactored createPromptForActivity using Text Blocks for readability ---
    private String createPromptForActivity(Activity activity, UserResponse userProfile) {
        // Use Java Text Blocks for cleaner, multi-line string definition
        String jsonSchema = """
    ```json
    {
      "analysis": {
        "height": "User's height in cm",
        "currentWeight": "User's current weight in kg",
        "goalWeight": "User's goal weight in kg",
        "age": "User's age in years",
        "dietType": "User's diet preference (e.g., VEGETARIAN, NON_VEGETARIAN)",
        "overall": "Detailed analysis based on user's profile and the current activity performed, including fitness progress and areas for improvement"
      },
      "improvements": [
        {
          "area": "Target area (e.g., Endurance, Pace, Intensity, Consistency)",
          "recommendation": "Single, clear recommendation for improving this specific area"
        }
      ],
      "suggestions": [
        {
          "workout": "Suggested workout (e.g., Interval Run, Long Ride, Hill Training)",
          "description": "One detailed description of the workout and its benefit"
        }
      ],
      "safety": [
        "Single safety guideline",
        "Another safety guideline"
      ]
    }
    ```
    """;

        String prompt = String.format("""
                Analyze this fitness activity and provide detailed recommendations.
                **The response MUST be in the following EXACT JSON format. STRICTLY adhere to this structure.**
                **Each item in 'improvements' and 'suggestions' arrays must be a single, distinct JSON object. DO NOT concatenate multiple recommendations into one object's 'recommendation' or 'description' field.**

                %s

                Analyze this activity:
                Activity Type: %s
                Duration: %d minutes
                Calories Burned: %d
                Additional Metrics: %s

                User Personal Profile (for personalized recommendations):
                First Name: %s
                Age: %d years
                Height: %.2f cm
                Current Weight: %.2f kg
                Goal Weight: %.2f kg
                Daily Water Intake: %.2f liters
                Diet Type: %s

                Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                Take into account the user's personal profile (age, height, current weight, goal weight, water intake, diet type) for more tailored recommendations.
                Crucially, ensure each item in 'improvements' and 'suggestions' arrays is a single, distinct JSON object following the schema, NOT concatenated strings or multiple recommendations in one object. List each improvement and suggestion as a separate, individual item.
                """,
                jsonSchema, // Insert the defined JSON schema
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics(),
                userProfile.getFirstName(), // Include name for a more personal tone
                userProfile.getAge(),
                userProfile.getHeightCm(),
                userProfile.getWeightKg(),
                userProfile.getGoalWeightKg(),
                userProfile.getDailyWaterIntakeLiters(),
                userProfile.getDietType().name() // Use .name() for enum
        );

        return prompt;
    }
}