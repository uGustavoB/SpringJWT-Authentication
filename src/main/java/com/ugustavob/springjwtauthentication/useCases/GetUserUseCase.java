package com.ugustavob.springjwtauthentication.useCases;

import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.exceptions.UserNotFoundException;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import com.ugustavob.springjwtauthentication.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {
    private final UserRepository userRepository;

    public UserEntity execute(UUID id) {
        System.out.println("id: " + id);
        UserEntity user = userRepository.findById(UUID.fromString(id.toString())).orElse(null);

        System.out.println("user: " + user);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }
}
