package com.ugustavob.springjwtauthentication.controllers;

import com.ugustavob.springjwtauthentication.dto.AssignRoleRequestDTO;
import com.ugustavob.springjwtauthentication.dto.GetAllUsersResponseDTO;
import com.ugustavob.springjwtauthentication.dto.RegisterRequestDTO;
import com.ugustavob.springjwtauthentication.dto.GetUserResponseDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.exceptions.UserNotFoundException;
import com.ugustavob.springjwtauthentication.useCases.role.AssignRoleUseCase;
import com.ugustavob.springjwtauthentication.useCases.user.DeleteUserUseCase;
import com.ugustavob.springjwtauthentication.useCases.user.GetAllUsersUseCase;
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
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final UpdateUserUseCase updateUserUseCase;

    @GetMapping("/me/")
    @Operation(summary = "Get user", description = "Get the authenticated user's details.")
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
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Unauthorized",
                                            summary = "Unauthorized",
                                            value = "Unauthorized"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User not found",
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

        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            UserEntity user = getUserUseCase.execute(id);
            return ResponseEntity.ok(new GetUserResponseDTO(user.getId(), user.getName(), user.getEmail()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/me/")
    @Operation(summary = "Update user", description = "Update user by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User updated",
                                            summary = "User updated",
                                            value = "User updated successfully"
                                    )
                            },
                            schema = @Schema(implementation = GetUserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Unauthorized",
                                            summary = "Unauthorized",
                                            value = "Unauthorized"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User not found",
                                            summary = "User not found",
                                            value = "User not found"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Unprocessable entity",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Unprocessable entity",
                                            summary = "Name is required",
                                            value = "Name is required"
                                    ),
                                    @ExampleObject(
                                            name = "Unprocessable entity",
                                            summary = "Email is required",
                                            value = "Email is required"
                                    ),
                                    @ExampleObject(
                                            name = "Unprocessable entity",
                                            summary = "Invalid email",
                                            value = "Invalid email"
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
            HttpServletRequest request
    ) {
        var id = (UUID) request.getAttribute("id");

        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            UserEntity user = updateUserUseCase.execute(new UserEntity(id, registerRequestDTO.name(),
                    registerRequestDTO.email(), registerRequestDTO.password(), null));
            return ResponseEntity.ok(new GetUserResponseDTO(user.getId(), user.getName(), user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users (Admin access required)."
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
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Unauthorized",
                                            summary = "Unauthorized",
                                            value = "Unauthorized"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden",
                                            summary = "User is not an admin",
                                            value = "User is not an admin"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Users not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Users not found",
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

                if (users == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users not found");
                }

                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not an admin");
            }

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete user",
            description = "Delete a user (Restricted to admins. Users cannot delete themselves)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User deleted",
                                            summary = "User deleted",
                                            value = "User deleted successfully"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Unauthorized",
                                            summary = "Unauthorized",
                                            value = "Unauthorized"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Forbidden",
                                            summary = "User is not an admin",
                                            value = "User is not an admin"
                                    ),
                                    @ExampleObject(
                                            name = "Forbidden",
                                            summary = "You can't delete yourself",
                                            value = "You can't delete yourself"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User not found",
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
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't delete yourself");
                }

                deleteUserUseCase.execute(uuid);

                return ResponseEntity.ok("User deleted");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not an admin");
            }

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Assign role",
            description = "Assign a new role to a user (Restricted to admins. A user cannot be assigned a role they already have)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Role assigned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Unauthorized",
                                            summary = "Unauthorized",
                                            value = "Unauthorized"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "User not found",
                                            summary = "User not found",
                                            value = "User not found"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Conflict",
                                            summary = "User already has role",
                                            value = "User already has role"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Unprocessable entity",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Unprocessable entity",
                                            summary = "Role is required",
                                            value = "Role is required"
                                    )
                            },
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @Schema(name = "AssignRoleRequestDTO", implementation = AssignRoleRequestDTO.class)
    @SecurityRequirement(name = "bearer")
    @PostMapping("/{uuid}/roles")
    public ResponseEntity<Object> assignRole(
            HttpServletRequest request,
            @Valid @RequestBody AssignRoleRequestDTO assignRoleRequestDTO,
            @PathVariable String uuid
    ) {
        var id = (UUID) request.getAttribute("id");

        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            UserEntity user = getUserUseCase.execute(id);

            if (user.getRole().contains("ROLE_ADMIN")) {
                UserEntity updatedUser = assignRoleUseCase.execute(assignRoleRequestDTO, UUID.fromString(uuid));

                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.badRequest().body("User is not an admin");
            }

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
