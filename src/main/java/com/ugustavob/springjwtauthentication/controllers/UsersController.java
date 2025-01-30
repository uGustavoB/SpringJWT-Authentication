package com.ugustavob.springjwtauthentication.controllers;

import com.ugustavob.springjwtauthentication.dto.AssignRoleRequestDTO;
import com.ugustavob.springjwtauthentication.dto.GetAllUsersResponseDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.useCases.role.AssignRoleUseCase;
import com.ugustavob.springjwtauthentication.useCases.user.DeleteUserUseCase;
import com.ugustavob.springjwtauthentication.useCases.user.GetAllUsersUseCase;
import com.ugustavob.springjwtauthentication.useCases.user.GetUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for users")
public class UsersController {
    private final GetUserUseCase getUserUseCase;
    private final AssignRoleUseCase assignRoleUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @Operation(
            summary = "Get all users",
            description = "Get all Users (Restricted to admins)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetAllUsersResponseDTO.class)
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
                                            summary = "Users not found",
                                            value = "Users not found"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @Schema(name = "UserEntity", implementation = UserEntity.class)
    @SecurityRequirement(name = "bearer")
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        var id = (UUID) request.getAttribute("id");

        try {
            UserEntity user = getUserUseCase.execute(id);

            if (user.getRole().contains("ROLE_ADMIN")) {
                List<GetAllUsersResponseDTO> users = getAllUsersUseCase.execute();
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.badRequest().body("User is not an admin");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete user",
            description = "Delete a user (Restricted to admins)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid request",
                                            summary = "You can't delete yourself",
                                            value = "You can't delete yourself"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid request",
                                            summary = "User is not an admin",
                                            value = "User is not an admin"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid request",
                                            summary = "User not found",
                                            value = "User not found"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @SecurityRequirement(name = "bearer")
    @Transactional
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @PathVariable UUID uuid) {
        var id = (UUID) request.getAttribute("id");

        try {
            UserEntity user = getUserUseCase.execute(id);

            if (user.getRole().contains("ROLE_ADMIN")) {
                if (id.toString().equals(uuid.toString())) {
                    return ResponseEntity.badRequest().body("You can't delete yourself");
                }

                deleteUserUseCase.execute(uuid);

                return ResponseEntity.ok("User deleted");
            } else {
                return ResponseEntity.badRequest().body("User is not an admin");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Assign role",
            description = "Assign a role to a user (Restricted to admins)"
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
    @PostMapping("/assign-role/{uuid}")
    public ResponseEntity<Object> assignRole(
            HttpServletRequest request,
            @Valid @RequestBody AssignRoleRequestDTO assignRoleRequestDTO,
            @PathVariable String uuid
    ) {
        var id = (UUID) request.getAttribute("id");

        try {
            UserEntity user = getUserUseCase.execute(id);

            if (user.getRole().contains("ROLE_ADMIN")) {
                UserEntity updatedUser = assignRoleUseCase.execute(assignRoleRequestDTO, UUID.fromString(uuid));

                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.badRequest().body("User is not an admin");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
