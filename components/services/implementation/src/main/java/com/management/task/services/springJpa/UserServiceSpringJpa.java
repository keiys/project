package com.management.task.services.springJpa;

import com.management.task.services.exceptions.userexceptions.UserAlreadyExcistException;
import com.management.task.services.exceptions.userexceptions.UserApiException;
import com.management.task.services.exceptions.userexceptions.UserBadRequestException;
import com.management.task.services.exceptions.userexceptions.UserNotFoundException;
import com.management.task.services.requests.UserRequest;
import com.management.task.services.responses.UserResponse;
import com.management.task.services.security.NoAuthenticationRequired;
import com.management.task.services.services.UserService;
import com.management.task.services.entity.UserEntity;
import com.management.task.services.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceSpringJpa implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if(userRequest.getUserId() != null){
            throw new UserBadRequestException("User id must be null");
        }

        if (StringUtils.isBlank(userRequest.getPassword())) {
            throw new UserBadRequestException("Password cannot be empty");
        }

        validateFields(userRequest);
        validatePassword(userRequest.getPassword());
        validateDuplicates(null, userRequest);
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        try{
            return userRepository.save(new UserEntity(userRequest)).toUserResponse();
        }catch (Exception ex){
            throw new UserApiException("Error occurred while trying to save user", ex);
        }

    }

    @Override
    public Page<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserResponse> result = new ArrayList<>();
        long count;
        try{
            count = userRepository.count();
            Page<UserEntity> userEntities = userRepository.findAll(pageable);
            userEntities.forEach(userEntity -> {
                result.add(userEntity.toUserResponse());
            });
        }catch (Exception ex){
            throw new UserApiException("Error occurred while trying to get all users", ex);
        }

        return new PageImpl<>(result, pageable, count);

//        return userRepository.findAll()
//                .stream()
//                .map(UserEntity::toUserResponse)
//                .toList();
    }

    @Override
    public UserResponse findUserById(UUID userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));
        return userEntity.toUserResponse();

    }

    @Override
    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        if (userRequest.getUserId() != null && !userRequest.getUserId().equals(userId)) {
            throw new UserBadRequestException("User ID in path doesn't match ID of user object");
        }

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

       if (StringUtils.isNotBlank(userRequest.getPassword())) {
           throw new UserBadRequestException("Password should be empty or null");
       }

        validateFields(userRequest);
        validateDuplicates(userId, userRequest);
        userEntity.setName(userRequest.getName());
        userEntity.setSurname(userRequest.getSurname());
        userEntity.setEmail(userRequest.getEmail());

        try{
            return userRepository.save(userEntity).toUserResponse();
        }catch (Exception ex){
            throw new UserApiException("Error occurred while trying to update user", ex);
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        try{
            userRepository.deleteById(userId);
        }catch (Exception ex){
            throw new UserApiException("Error occurred while trying to delete user", ex);
        }

    }

    @Override
    public UserResponse updatePassword(String email, String oldPassword, String newPassword, String confirmPassword) {
        if (!StringUtils.isBlank(newPassword) && !newPassword.equals(confirmPassword)) {
            throw new UserBadRequestException("Password don't match");
        }
        UserEntity userEntity = userRepository.getByEmail(email);
        if(userEntity == null){
            throw new UserNotFoundException("User not found with email: " + email);
        }
        if(StringUtils.isBlank(oldPassword) && !(passwordEncoder.encode(oldPassword).equals(userEntity.getPassword()))){
            throw new UserBadRequestException("Wrong old password");
        }

        try{
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(userEntity).toUserResponse();
        }catch (Exception ex){
            throw new UserApiException("Error occurred while trying to change password", ex);
        }
    }

    private void validatePassword(String password){

        if(password != null && password.length() < 8){
            throw new UserBadRequestException("Password must be at least 8 characters");
        }
        AtomicInteger uppercaseCounter = new AtomicInteger();
        AtomicInteger digit = new AtomicInteger();
        AtomicInteger specialChar = new AtomicInteger();

        password.chars().forEach(c -> {
            if(Character.isUpperCase(c)) {
                uppercaseCounter.getAndIncrement();
            }else if (Character.isDigit(c)) {
                digit.getAndIncrement();
            }else if (!Character.isLetterOrDigit(c)) {
                specialChar.getAndIncrement();
            }
        });
        if (uppercaseCounter.get() < 1 ){
            throw new UserBadRequestException("Password must be at least 1 uppercase character");
        }
        if (digit.get() < 2 ){
            throw new UserBadRequestException("Password must be at least 2 digits characters");
        }
        if (specialChar.get() < 1 ){
            throw new UserBadRequestException("Password must be at least 1 special character");
        }
    }

    private void validateFields(UserRequest userRequest) {
        if(userRequest.getVerifyCode() != null){
            throw new UserBadRequestException("Verify code must be null");
        }
        if(userRequest.getStatus() != null){
            throw new UserBadRequestException("Status must be null");
        }
    }

    private void validateDuplicates(UUID id, UserRequest userRequest) {
        UserEntity userEntity;

        try{
            if(id == null){
                userEntity = userRepository.getByEmail(userRequest.getEmail());
            }else{
                userEntity = userRepository.findByEmailAndUserIdNot(userRequest.getEmail(), id);
            }
        }catch (Exception ex){
            throw new UserApiException("Error occurred while trying to find user with email: " + userRequest.getEmail(), ex);
        }

        if(userEntity != null){
            throw new UserAlreadyExcistException("User already excist with given email");
        }

    }
}
