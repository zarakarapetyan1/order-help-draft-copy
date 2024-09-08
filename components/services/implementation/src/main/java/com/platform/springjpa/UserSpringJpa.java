package com.platform.springjpa;

import com.platform.entity.AccountEntity;
import com.platform.entity.UserEntity;
import com.platform.exceptions.userExceptions.UserAlreadyExistException;
import com.platform.exceptions.userExceptions.UserApiException;
import com.platform.exceptions.userExceptions.UserBadRequestException;
import com.platform.exceptions.userExceptions.UserNotFoundException;
import com.platform.model.User;
import com.platform.repository.AccountRepository;
import com.platform.repository.UserRepository;
import com.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSpringJpa implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User getById(UUID userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with given ID");
        }
        return (User) optionalUser.get().toUser();
    }

    @Override
    public User createUser(User user) {
        UserEntity userEntity;
        Optional<AccountEntity> accountEntity;

        if (user.getUserId() != null) {
            throw new UserBadRequestException("User ID must be null");
        }

        if(user.getPassword() != null) {
            throw new UserBadRequestException("Password must be null");
        }

        try {
            userEntity = userRepository.getByEmail(user.getEmail());
        } catch (Exception ex) {
            throw new UserApiException("Problems occurred during creating user...");
        }

        if (userEntity != null) {
            throw new UserAlreadyExistException("User already exists with given email");
        }

        try {
            accountEntity = accountRepository.findById(user.getAccountId());
        } catch (Exception e) {
            throw new UserApiException("Problems occurred while creating user");
        }

        if (accountEntity.isEmpty()) {
            throw new UserNotFoundException("Account not found with given ID");
        }

        try {
            UserEntity newUserEntity = new UserEntity(user);
            newUserEntity.setAccountEntity(accountEntity.get());
            userEntity = userRepository.save(newUserEntity);
        } catch (Exception ex) {
            throw new UserApiException("Problems occurred during creating user...");
        }

        return userEntity.toUser();
    }


    @Override
    public List<User> getUsers() {
        List<UserEntity> userEntities;
        try {
            userEntities = userRepository.findAll();
        } catch (Exception ex) {
            throw new UserApiException("Problems occurred during getting users...");

        }
        return userEntities
                .stream()
                .map(UserEntity::toUser)
                .toList();

    }

    @Override
    public List<User> getUsersByEmail(String email) {

        try {
            UserEntity userEntity = userRepository.getByEmail(email);
            return List.of(userEntity.toUser());
        } catch (Exception ex) {
            throw new UserApiException("Problems occurred during getting users...");
        }
    }

    @Override
    public User updateUser(UUID userId, User user) {
        Optional<UserEntity> userEntities;
        UserEntity existingUser;

        if(user.getPassword() != null) {
            throw new UserBadRequestException("Password must be null");
        }
        
        if (user.getUserId() != null && !user.getUserId().equals(userId)) {
            throw new UserBadRequestException("ID of body doesn't match with URL parameter");
        }

        try {
            userEntities = userRepository.findById(userId);
            existingUser = userRepository.getByEmailAndUserIdNot(user.getEmail(), userId);
        } catch (Exception e) {
            throw new UserApiException("Problem during updating user");
        }

        if (userEntities.isEmpty()) {
            throw new UserNotFoundException("User not found with given ID");
        }

        if (existingUser != null) {
            throw new UserAlreadyExistException("User already exists with given email.");
        }

        AccountEntity account = getAccount(user.getAccountId());
        User updatedUser;

        try {
            UserEntity userEntity = userEntities.get();
            userEntity.setAccountEntity(account);
            updatedUser = userRepository.save(userEntity).toUser();
        } catch (Exception e) {
            throw new UserApiException("Problem during updating user");
        }

        return updatedUser;
    }


    @Override
    public void deleteUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with given ID"));
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new UserApiException("Problem during deleting user");
        }
    }

    private AccountEntity getAccount(UUID accountId) {
        Optional<AccountEntity> accountEntity;
        try {
            accountEntity = accountRepository.findById(accountId);
        } catch (Exception e) {
            throw new UserApiException("Problem during creating user");
        }
        if (accountEntity.isEmpty()) {
            throw new UserNotFoundException("Account not found with given account ID");
        }
        return accountEntity.get();
    }


}
