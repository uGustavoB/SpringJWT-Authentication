package com.ugustavob.springjwtauthentication.controllers;

import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.useCases.GetUserUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final GetUserUseCase getUserUseCase;

    @GetMapping("/")
    public ResponseEntity<Object> getUser(HttpServletRequest request) {
        var id = (UUID) request.getAttribute("id");

        try {
            UserEntity user = getUserUseCase.execute(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
