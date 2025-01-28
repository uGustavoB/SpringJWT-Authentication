package com.ugustavob.springjwtauthentication.controllers;

import com.ugustavob.springjwtauthentication.dto.AssignRoleRequestDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.useCases.role.AssignRoleUseCase;
import com.ugustavob.springjwtauthentication.useCases.user.GetUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final GetUserUseCase getUserUseCase;
    private final AssignRoleUseCase assignRoleUseCase;

    @Operation(
            summary = "Assign role",
            description = "Assign a role to a user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Role assigned successfully",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid request",
                                            summary = "User is not an admin",
                                            value = "User is not an admin"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid request",
                                            summary = "User not found",
                                            value = "User not found"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid request",
                                            summary = "User already has role",
                                            value = "User already has role"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @Schema(name = "AssignRoleRequestDTO", implementation = AssignRoleRequestDTO.class)
    @SecurityRequirement(name = "bearer")
    @PostMapping("/assign/")
    public ResponseEntity<Object> assignRole(HttpServletRequest request, @Valid @RequestBody AssignRoleRequestDTO assignRoleRequestDTO) {
        var id = (UUID) request.getAttribute("id");

        try {
            UserEntity user = getUserUseCase.execute(id);

            if (user.getRole().contains("ROLE_ADMIN")) {
                UserEntity updatedUser = assignRoleUseCase.execute(assignRoleRequestDTO);

                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.badRequest().body("User is not an admin");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
