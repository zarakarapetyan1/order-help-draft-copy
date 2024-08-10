package com.platform.springjpa;

import com.platform.entity.AccountEntity;
import com.platform.entity.AddressEntity;
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

        try {
            userEntity = userRepository.getByEmail(user.getEmail());
        } catch (Exception ex) {
            throw new UserApiException("Problems occurred during creating user...");
        }

        if (userEntity != null) {
            throw new UserAlreadyExistException("User already exist with given email");
        }

        try{
             accountEntity = accountRepository.findById(user.getAccountId());
        }catch (Exception e){
            throw new UserApiException("Problems occurred while creating user");
        }

        if(accountEntity.isEmpty()){
            throw new UserNotFoundException("Account not found with given ID");
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UserEntity newUserEntity = new UserEntity(user);
            newUserEntity.setAccountEntity(accountEntity.get());
            userEntity=userRepository.save(newUserEntity);
        } catch (Exception ex) {
            throw new UserApiException("problems occurred during creating user...");

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
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if (optionalUserEntity.isEmpty()) {
            throw new UserNotFoundException("User not found with given ID");
        }

        UserEntity existingUserEntity = optionalUserEntity.get();

        if (user.getEmail() != null && !user.getEmail().equals(existingUserEntity.getEmail())) {
            UserEntity userEntityByEmail = userRepository.getByEmail(user.getEmail());
            if (userEntityByEmail != null) {
                throw new UserAlreadyExistException("User already exists with given email");
            }
        }

        if (user.getAccountId() != null) {
            AccountEntity accountEntity = accountRepository.findById(user.getAccountId())
                    .orElseThrow(() -> new UserNotFoundException("Account not found with given ID"));
            existingUserEntity.setAccountEntity(accountEntity);
        }

        if (user.getAddress() != null) {
            AddressEntity addressEntity = new AddressEntity(user.getAddress());
            existingUserEntity.setAddressEntity(addressEntity);
        }

        existingUserEntity.setName(user.getName());
        existingUserEntity.setSurname(user.getSurname());
        existingUserEntity.setEmail(user.getEmail());
        existingUserEntity.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            existingUserEntity = userRepository.save(existingUserEntity);
        } catch (Exception ex) {
            throw new UserApiException("Problems occurred during updating user");
        }

        return existingUserEntity.toUser();
    }

    @Override
    public void deleteUser(UUID userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with given ID"));
        try {
            userRepository.delete(userEntity);
        } catch (Exception ex) {
            throw new UserApiException("Problems occurred during deleting user");
        }
    }
}
