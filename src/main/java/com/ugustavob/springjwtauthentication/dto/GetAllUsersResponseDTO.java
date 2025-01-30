package com.ugustavob.springjwtauthentication.dto;

import java.util.Set;
import java.util.UUID;

public record GetAllUsersResponseDTO(
        UUID id,
        String name,
        String email,
        Set<String> role
) {
}
