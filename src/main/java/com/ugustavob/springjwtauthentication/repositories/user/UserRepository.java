package com.ugustavob.springjwtauthentication.repositories.user;

import com.ugustavob.springjwtauthentication.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
}
