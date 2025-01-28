package com.ugustavob.springjwtauthentication.controllers;

import com.ugustavob.springjwtauthentication.dto.RegisterRequestDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.useCases.user.GetUserUseCase;
import com.ugustavob.springjwtauthentication.useCases.user.UpdateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user")
public class UserController {
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @GetMapping("/")
    @Operation(summary = "Get user", description = "Get user by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Bad request",
                                            summary = "User not found",
                                            value = "User not found"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @Schema(name = "UserEntity", implementation = UserEntity.class)
    @SecurityRequirement(name = "bearer")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        var id = (UUID) request.getAttribute("id");

        try {
            UserEntity user = getUserUseCase.execute(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/")
    @Operation(summary = "Update user", description = "Update user by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Bad request",
                                            summary = "User not found",
                                            value = "User not found"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @Schema(name = "UserEntity", implementation = UserEntity.class)
    @SecurityRequirement(name = "bearer")
    public ResponseEntity<?> updateUser(
            @Valid @RequestBody
            RegisterRequestDTO registerRequestDTO,
            HttpServletRequest request) {
        var id = (UUID) request.getAttribute("id");

        try {
            UserEntity user = updateUserUseCase.execute(new UserEntity(id, registerRequestDTO.name(),
                    registerRequestDTO.email(), registerRequestDTO.password(), null));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
