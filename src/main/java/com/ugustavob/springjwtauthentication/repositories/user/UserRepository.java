package com.ugustavob.springjwtauthentication.repositories.user;

import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findById(UUID id);
    Optional<UserEntity> findByEmail(String email);
}
