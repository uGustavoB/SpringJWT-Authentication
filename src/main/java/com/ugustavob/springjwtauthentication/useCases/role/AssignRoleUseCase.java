package com.ugustavob.springjwtauthentication.useCases.role;

import com.ugustavob.springjwtauthentication.dto.AssignRoleRequestDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import com.ugustavob.springjwtauthentication.useCases.user.GetUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignRoleUseCase {
    private final GetUserUseCase getUserUseCase;
    private final UserRepository userRepository;

    public UserEntity execute(@Valid AssignRoleRequestDTO assignRoleRequestDTO) {
        UserEntity user = getUserUseCase.execute(assignRoleRequestDTO.userId());

        if (user.getRole().contains("ROLE_" + assignRoleRequestDTO.role().toUpperCase())) {
            throw new RuntimeException("User already has this role");
        }
        user.getRole().add("ROLE_" + assignRoleRequestDTO.role().toUpperCase());

        return userRepository.save(user);
    }
}
