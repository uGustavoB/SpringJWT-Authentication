package com.ugustavob.springjwtauthentication.controllers;

import com.ugustavob.springjwtauthentication.dto.LoginRequestDTO;
import com.ugustavob.springjwtauthentication.dto.LoginResponseDTO;
import com.ugustavob.springjwtauthentication.dto.RegisterRequestDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import com.ugustavob.springjwtauthentication.security.TokenService;
import com.ugustavob.springjwtauthentication.useCases.CreateUserUseCase;
import com.ugustavob.springjwtauthentication.useCases.LoginUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginUserUseCase loginUserUseCase;
    private final TokenService tokenService;
    private final CreateUserUseCase createUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            UserEntity user = loginUserUseCase.execute(loginRequest);
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getName(), token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO body){

        try {
            UserEntity newUser = createUserUseCase.execute(body);
            String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new LoginResponseDTO(newUser.getName(), token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
