package com.ugustavob.springjwtauthentication.useCases.user;

import com.ugustavob.springjwtauthentication.dto.GetAllUsersResponseDTO;
import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase {
    private final UserRepository userRepository;

    public List<GetAllUsersResponseDTO> execute() {
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            return null;
        }

        return users.stream().map(user -> new GetAllUsersResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        )).toList();
    }
}
