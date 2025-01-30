package com.ugustavob.springjwtauthentication.useCases.user;

import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.exceptions.UserNotFoundException;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {
    private final UserRepository userRepository;

    public void execute(UUID id) {
        UserEntity deletedUser = userRepository.deleteByIdAndReturnEntity(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
