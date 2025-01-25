package com.ugustavob.springjwtauthentication.controllers;

import com.ugustavob.springjwtauthentication.entities.user.User;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        // Gustavo - Futuramente, implementar um DTO para retornar os dados do usuário logado conforme o token
        var id = (UUID) request.getAttribute("id");

        User user = userRepository.findById(UUID.fromString(id.toString())).orElse(null);

        if (user == null) {
            System.out.println("not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user);
    }
}
