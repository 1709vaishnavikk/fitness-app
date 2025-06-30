package com.fitness.userService.repository;

import com.fitness.userService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmail(String email);

    Boolean existsByKeycloakId(String userId);

    User findByEmail(String email);

    User findByKeycloakId(String keycloakId);
}
