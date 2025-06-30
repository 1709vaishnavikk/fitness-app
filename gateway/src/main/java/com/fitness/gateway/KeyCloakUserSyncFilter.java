package com.fitness.gateway;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeyCloakUserSyncFilter implements WebFilter {
    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        String userIdFromHeader = exchange.getRequest().getHeaders().getFirst("X-User-ID"); // Renamed for clarity

        if (token == null) {
            log.warn("No Authorization token found. Proceeding without user sync.");
            return chain.filter(exchange);
        }

        log.info("Token: {}", token);

        RegisterRequest registerRequest = getUserDetails(token);

        if (registerRequest == null) {
            log.error("Failed to parse user details from token. Proceeding without user sync.");
            return chain.filter(exchange);
        }

        String userIdFromToken = registerRequest.getKeycloakId();
        log.info("Parsed from token: {}", registerRequest);

        String finalUserId = (userIdFromHeader != null && !userIdFromHeader.isEmpty()) ? userIdFromHeader : userIdFromToken;
        log.info("Resolved userId for sync: {}", finalUserId);

        return userService.validateUser(finalUserId)
                .flatMap(userExists -> {
                    if (userExists) {
                        log.info("User already exists: {}, Skipping sync.", finalUserId);
                        return Mono.just(true); // Signal that the user is handled (exists)
                    } else {
                        log.info("User does not exist. Attempting to register user: {}", finalUserId);
                        // Register user if they don't exist
                        return userService.registerUser(registerRequest)
                                .doOnNext(resp -> log.info("User registered successfully: {}", resp.getKeycloakId()))
                                .map(resp -> true) // <--- SUCCESS CASE: Map UserResponse to true (user handled)
                                .onErrorResume(err -> {
                                    log.error("Error while registering user {}: {}", finalUserId, err.getMessage());
                                    return Mono.just(false); // <--- ERROR CASE: Map error to false (user not handled)
                                });
                    }
                })
                .flatMap(isUserHandled -> {
                    if (isUserHandled) {
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-ID", finalUserId)
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    } else {
                        log.warn("User sync failed or was skipped for {}. Proceeding without X-User-ID header.", finalUserId);
                        return chain.filter(exchange);
                    }
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Unhandled error during KeyCloak user sync filter: {}", e.getMessage());
                    return chain.filter(exchange);
                });
    }

    private RegisterRequest getUserDetails(String token) {
        try {
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(claims.getStringClaim("email"));
            registerRequest.setKeycloakId(claims.getStringClaim("sub"));
            registerRequest.setPassword("dummy@123123");
            registerRequest.setFirstName(claims.getStringClaim("given_name"));
            registerRequest.setLastName(claims.getStringClaim("family_name"));
            return registerRequest;
        } catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            return null;
        }
    }
}