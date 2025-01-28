package com.ugustavob.springjwtauthentication.useCases.user;

import com.ugustavob.springjwtauthentication.entities.user.UserEntity;
import com.ugustavob.springjwtauthentication.exceptions.UserNotFoundException;
import com.ugustavob.springjwtauthentication.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity execute(UserEntity user) {
        Optional<UserEntity> userOptional = userRepository.findById(user.getId());

        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();

            UserEntity alreadyExists = userRepository.findByEmail(user.getEmail()).orElse(null);

            if (alreadyExists != null && !alreadyExists.getId().equals(user.getId())) {
                throw new RuntimeException("Email already exists");
            }

            userEntity.setName(user.getName());
            userEntity.setEmail(user.getEmail());
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

            return userRepository.save(userEntity);
        }

        throw new UserNotFoundException();
    }
}
