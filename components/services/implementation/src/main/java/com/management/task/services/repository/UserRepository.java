package com.management.task.services.repository;

import com.management.task.services.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.apache.catalina.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity getByEmail(String email);

    Page<UserEntity> findAll(@NotNull Pageable pageable);
    long count();

    UUID userId(UUID userId);

    UserEntity findByEmailAndUserIdNot(String email, UUID userId);
}
