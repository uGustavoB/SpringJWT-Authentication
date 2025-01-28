package com.ugustavob.springjwtauthentication.useCases.user;

import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.exceptions.UserNotFoundException;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {
    private final UserRepository userRepository;

    public UserEntity execute(UUID id) {
        UserEntity user = userRepository.findById(UUID.fromString(id.toString())).orElse(null);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }
}
