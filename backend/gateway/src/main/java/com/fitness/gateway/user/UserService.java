package com.fitness.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return userServiceWebClient.get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    // This is the CRITICAL block where the "User Not Found" exception was thrown
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        log.info("User {} not found in user service. This is expected for new users.", userId);
                        // Instead of throwing an error, we return Mono.just(false)
                        // to signal that the user does not exist.
                        return Mono.just(false); // <--- THIS LINE IS THE FIX FOR THE "User Not Found" EXCEPTION
                    } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        log.error("Invalid Request for user {}: {}", userId, e.getMessage());
                        // For other client errors, it's generally good to propagate the error
                        return Mono.error(new RuntimeException("Invalid Request for user: " + userId));
                    } else {
                        log.error("Unexpected error validating user {}: {}", userId, e.getMessage());
                        // For unexpected server errors, propagate the error
                        return Mono.error(new RuntimeException("Unexpected error during user validation: " + e.getMessage()));
                    }
                })
                .doOnSuccess(exists -> log.info("User {} validation API result: {}", userId, exists));
    }

    public Mono<UserResponse> registerUser(RegisterRequest request) {
        log.info("Calling User Registration API for email: {}", request.getEmail());
        return userServiceWebClient.post()
                .uri("/api/users/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .doOnSuccess(resp -> log.info("Successfully registered user with keycloakId: {}", resp.getKeycloakId()))
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        log.error("Bad Request during user registration for {}: {}", request.getEmail(), e.getMessage());
                        return Mono.error(new RuntimeException("Bad Request during user registration: " + e.getMessage()));
                    } else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        log.error("Internal Server Error during user registration for {}: {}", request.getEmail(), e.getMessage());
                        return Mono.error(new RuntimeException("Internal Server Error during user registration: " + e.getMessage()));
                    } else {
                        log.error("Unexpected error during user registration for {}: {}", request.getEmail(), e.getMessage());
                        return Mono.error(new RuntimeException("Unexpected error during user registration: " + e.getMessage()));
                    }
                });
    }
}