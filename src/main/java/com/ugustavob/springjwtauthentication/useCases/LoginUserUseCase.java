package com.ugustavob.springjwtauthentication.useCases;

import com.ugustavob.springjwtauthentication.dto.LoginRequestDTO;
import com.ugustavob.springjwtauthentication.dto.LoginResponseDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.exceptions.InvalidCredentialsException;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import com.ugustavob.springjwtauthentication.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginUserUseCase {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public UserEntity execute(@Valid LoginRequestDTO loginRequest) {
        Optional<UserEntity> user = userRepository.findByEmail(loginRequest.email());

        if (user.isPresent() && passwordEncoder.matches(loginRequest.password(), user.get().getPassword())) {
            String token = tokenService.generateToken(user.get());
            return user.get();
        }

        throw new InvalidCredentialsException();
    }
}
