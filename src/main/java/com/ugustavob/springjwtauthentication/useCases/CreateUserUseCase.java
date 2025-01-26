package com.ugustavob.springjwtauthentication.useCases;

import com.ugustavob.springjwtauthentication.dto.RegisterRequestDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import com.ugustavob.springjwtauthentication.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserEntity execute(@Valid RegisterRequestDTO registerRequest) {
        Optional<UserEntity> user = userRepository.findByEmail(registerRequest.email());

        if (user.isEmpty()) {
            UserEntity newUser = new UserEntity();
            newUser.setName(registerRequest.name());
            newUser.setEmail(registerRequest.email());
            newUser.setPassword(passwordEncoder.encode(registerRequest.password()));

            String token = tokenService.generateToken(newUser);
            return userRepository.save(newUser);
        }

        throw new RuntimeException("User already exists");
    }
}
