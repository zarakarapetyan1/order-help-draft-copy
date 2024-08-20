package com.platform.springjpa;

import com.platform.entity.UserEntity;
import com.platform.exceptions.userExceptions.UserApiException;
import com.platform.exceptions.userExceptions.UserNotFoundException;
import com.platform.repository.UserRepository;
import com.platform.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordSpringJpa implements PasswordService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(UUID userId, String newPassword) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with given ID"));

        try {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new UserApiException("Problem during changing password");
        }
    }
}
