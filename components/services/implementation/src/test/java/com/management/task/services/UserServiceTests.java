package com.management.task.services;

import com.management.task.services.Enums.Role;
import com.management.task.services.Enums.Status;
import com.management.task.services.entity.UserEntity;
import com.management.task.services.exceptions.userexceptions.UserBadRequestException;
import com.management.task.services.exceptions.userexceptions.UserNotFoundException;
import com.management.task.services.repository.UserRepository;
import com.management.task.services.requests.UserRequest;
import com.management.task.services.responses.UserResponse;
import com.management.task.services.services.UserService;
import com.management.task.services.springJpa.UserServiceSpringJpa;
import org.apache.catalina.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceSpringJpa userService;

    @Test
    void testUser_createUser() {
        UserRequest userRequest = UserRequest.builder()
                .name("Yuuuna")
                .surname("Yan")
                .email("yuuna@gmail.com")
                .password("Example_123")
                .role(Role.ADMIN)
                .build();

        String encodedPassword = "ENCODED_PASS";
        given(passwordEncoder.encode("Example_123")).willReturn(encodedPassword);

        UserEntity savedEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .email(userRequest.getEmail())
                .password(encodedPassword)
                .role(userRequest.getRole())
                .status(Status.ACTIVE)
                .build();

        given(userRepository.save(any(UserEntity.class))).willReturn(savedEntity);
        UserResponse userResponse = userService.createUser(userRequest);
        assertNotNull(userResponse);
        assertEquals("Yuuuna", userResponse.name());
        assertEquals("Yan", userResponse.surname());
        assertEquals("yuuna@gmail.com", userResponse.email());
        assertEquals(Status.ACTIVE, userResponse.status());
        assertEquals(Role.ADMIN, userResponse.role());


    }

    @Test
    void testUser_createUser_IdNotNull() {
        UUID userId = UUID.randomUUID();
        UserRequest userRequest = UserRequest.builder()
                .userId(userId)
                .build();

        UserBadRequestException userBadRequestException = assertThrows(UserBadRequestException.class, () ->
                userService.createUser(userRequest));

        assertEquals("User id must be null", userBadRequestException.getMessage());
    }

    @Test
    void testUser_createUser_PasswordNull() {
        UserRequest userRequest = new UserRequest();

        UserBadRequestException userBadRequestException = assertThrows(UserBadRequestException.class, () ->
                userService.createUser(userRequest));

        assertEquals("Password cannot be empty", userBadRequestException.getMessage());
    }

    @Test
    void testUser_getById() {
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .name("Yuna")
                .surname("Kim")
                .email("yuna@gmail.com")
                .password("Example_112")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));

        UserResponse userResponse = userService.findUserById(userId);
        assertEquals(userResponse.userId(), userId);
        assertEquals(userResponse.name(), userEntity.getName());
        assertEquals(userResponse.surname(), userEntity.getSurname());
        assertEquals(userResponse.password(), userEntity.getPassword());
        assertEquals(userResponse.email(), userEntity.getEmail());
            assertEquals(userResponse.role(), userEntity.getRole());
        assertEquals(userResponse.status(), userEntity.getStatus());

        //verify(userRepository, times(1)).findById

    }

    @Test
    void testUser_getById_notFound() {
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () ->
                userService.findUserById(userId));
        assertEquals(userNotFoundException.getMessage(), "User not found with id: " + userId);
    }

    @Test
    void testUser_getAllUsers() {

        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        UserEntity userEntity1 = UserEntity.builder()
                .userId(UUID.randomUUID())
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .name("Yuna")
                .surname("Kim")
                .email("yuna@gmail.com")
                .password("Example_112")
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .userId(UUID.randomUUID())
                .role(Role.USER)
                .status(Status.ACTIVE)
                .name("John")
                .surname("Smith")
                .email("john@gmail.com")
                .password("Example_112")
                .build();

        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);
        Page<UserEntity> entityPage = new PageImpl<>(userEntities, pageable, userEntities.size());
        given(userRepository.count()).willReturn((long) userEntities.size());
        given(userRepository.findAll(pageable)).willReturn(entityPage);

        Page<UserResponse> userResponse = userService.getAllUsers(page, size);
        assertNotNull(userResponse);
        assertEquals(2, userResponse.getContent().size());
        assertEquals("Yuna", userResponse.getContent().get(0).name());
        assertEquals("John", userResponse.getContent().get(1).name());
        assertEquals(2, userResponse.getTotalElements());
    }

    @Test
    void testUser_updateUser() {
        UUID userId = UUID.randomUUID();
        UserRequest userRequest = UserRequest.builder()
                .name("UpdatedName")
                .surname("UpdatedSurname")
                .email("updated@gmail.com")
                .build();

        UserEntity existingUser = UserEntity.builder()
                .userId(userId)
                .name("OldName")
                .surname("OldSurname")
                .email("old@gmail.com")
                .build();

        UserEntity savedUser = UserEntity.builder()
                .userId(userId)
                .name("UpdatedName")
                .surname("UpdatedSurname")
                .email("updated@gmail.com")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(userRepository.save(existingUser)).willReturn(savedUser);

        UserResponse userResponse = userService.updateUser(userId, userRequest);

        assertNotNull(userResponse);
        assertEquals("UpdatedName", userResponse.name());
        assertEquals("UpdatedSurname", userResponse.surname());
        assertEquals("updated@gmail.com", userResponse.email());
    }

    @Test
    void testUser_updateUser_passwordNotEmpty() {
        UUID userId = UUID.randomUUID();
        UserRequest userRequest = UserRequest.builder()
                .password("Example_123")
                .build();
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        UserBadRequestException userBadRequestException = assertThrows(UserBadRequestException.class, () ->
                userService.updateUser(userId, userRequest));

        assertEquals("Password should be empty or null", userBadRequestException.getMessage());
    }

    @Test
    void testUser_updateUser_notFound() {
        UUID userId = UUID.randomUUID();
        UserRequest userRequest = new UserRequest();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () ->
                userService.updateUser(userId, userRequest));

        assertEquals("User not found with id: " + userId, userNotFoundException.getMessage());
    }

    @Test
    void testUser_updateUser_userIdMismatch() {
        UUID userPathId = UUID.randomUUID();
        UUID userRequestId = UUID.randomUUID();
        UserRequest userRequest = UserRequest.builder()
                .userId(userRequestId)
                .build();

        UserBadRequestException userBadRequestException = assertThrows(UserBadRequestException.class, () ->
                userService.updateUser(userPathId, userRequest));

        assertEquals("User ID in path doesn't match ID of user object", userBadRequestException.getMessage());


    }

    @Test
    void testUser_deleteUser() {
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));

        assertDoesNotThrow(() -> userService.deleteUser(userId));


    }

    @Test
    void testUser_deleteUser_userNotFound() {
        UUID userId = UUID.randomUUID();
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () ->
                userService.deleteUser(userId));

        assertEquals("User not found with id: " + userId, userNotFoundException.getMessage());
    }

    @Test
    void testUser_updatePassword() {
        String email = "yuna@gmail.com";
        String oldPassword = "OldPass_123";
        String newPassword = "NewPass_123";
        String confirmPassword = "NewPass_123";

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password("EncodedOldPass")
                .build();

        UserEntity savedUser = UserEntity.builder()
                .email(email)
                .password("EncodedNewPass")
                .build();

        given(userRepository.getByEmail(email)).willReturn(userEntity);
        given(passwordEncoder.encode(oldPassword)).willReturn("EncodedOldPass");
        given(passwordEncoder.encode(newPassword)).willReturn("EncodedNewPass");
        given(userRepository.save(userEntity)).willReturn(savedUser);

        UserResponse userResponse = userService.updatePassword(email, oldPassword, newPassword, confirmPassword);
        assertEquals("EncodedNewPass", userResponse.password());
    }

    @Test
    void testUser_updatePassword_passwordNotMatch() {
        String email = "yuna@gmail.com";
        String oldPassword = "OldPass123";
        String newPassword = "NewPass123";
        String confirmPassword = "MismatchPass";

        UserBadRequestException userBadRequestException = assertThrows(UserBadRequestException.class, () ->
                userService.updatePassword(email, oldPassword, newPassword, confirmPassword));

        assertEquals("Password don't match", userBadRequestException.getMessage());
    }

    @Test
    void testUser_updatePassword_userNotFound() {
        String email = "unknown@gmail.com";
        String oldPassword = "OldPass123";
        String newPassword = "NewPass123";
        String confirmPassword = "NewPass123";

        given(userRepository.getByEmail(email)).willReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.updatePassword(email, oldPassword, newPassword, confirmPassword));

        assertEquals("User not found with email: " + email, exception.getMessage());
    }

    @Test
    void testUser_updatePassword_wrongOldPassword() {
        String email = "yuna@gmail.com";
        String oldPassword = "WrongOld";
        String newPassword = "NewPass123";
        String confirmPassword = "NewPass123";

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password("EncodedOldPass")
                .build();

        given(userRepository.getByEmail(email)).willReturn(userEntity);
        given(passwordEncoder.encode(oldPassword)).willReturn("EncodedWrongOld");

        UserBadRequestException userBadRequestException = assertThrows(UserBadRequestException.class, () ->
                userService.updatePassword(email, oldPassword, newPassword, confirmPassword));

        assertEquals("Wrong old password", userBadRequestException.getMessage());
    }

}
