package com.ugustavob.springjwtauthentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AssignRoleRequestDTO(
        @Schema(description = "Role", example = "ROLE_USER")
        @NotBlank(message = "Role is required")
        String role
) {
}
