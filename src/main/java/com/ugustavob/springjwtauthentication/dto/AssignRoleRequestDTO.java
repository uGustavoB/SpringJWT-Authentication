package com.ugustavob.springjwtauthentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record AssignRoleRequestDTO(
        @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID userId,
        @Schema(description = "Role", example = "ROLE_USER")
        @NotBlank(message = "Role is required")
        String role
) {
}
