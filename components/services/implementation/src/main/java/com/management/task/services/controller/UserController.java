package com.management.task.services.controller;

import com.management.task.ExceptionResponse;
import com.management.task.management.services.constants.RoutConstants;
import com.management.task.management.services.requests.UserRequest;
import com.management.task.management.services.responses.UserResponse;
import com.management.task.management.services.services.UserService;
import com.management.task.services.repository.UserRepository;
import com.management.task.services.security.RequiredAdminUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(RoutConstants.BASE_URL + "${task.service.verion}" + RoutConstants.USERS)
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create user with specified parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="201", description = "User created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }),
            @ApiResponse(responseCode ="400", description = "Invalid request to sent endpoint", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="409", description = "User already excist", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while creating user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @Valid UserRequest userRequest) {
        log.info("Received request to create user: {}", userRequest);
        UserResponse user = userService.createUser(userRequest);
        log.info("Created user: {}", user);
        return user;
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "Request was successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while creating user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @GetMapping
    public Page<UserResponse> getAllUsers(@RequestParam int page, @RequestParam int size) {
        log.info("Received request to get all users");
        Page<UserResponse> allUsers = userService.getAllUsers(page, size);
        log.info("All users: {}", allUsers);
        return allUsers;

    }

    @Operation(summary = "Get user by specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="201", description = "User found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }),
            @ApiResponse(responseCode ="400", description = "Invalid request to sent endpoint", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="404", description = "Entity not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while getting user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        log.info("Received request to get user by ID {}", id);
        UserResponse user = userService.findUserById(id);
        log.info("User {}", user);
        return user;
    }

    @Operation(summary = "Update user with specified parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "User updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }),
            @ApiResponse(responseCode ="400", description = "Invalid request to sent endpoint", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="404", description = "Entity not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="409", description = "User already excist", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while getting user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @PutMapping("/{userid}")
    public UserResponse updateUser(@PathVariable UUID userid, @RequestBody @Valid UserRequest userRequest) {
        log.info("Received request to update user with ID {}", userid);
        UserResponse user = userService.updateUser(userid, userRequest);
        log.info("User updated: {}", userRequest);
        return user;
    }


    @Operation(summary = "Delete user by given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="204", description = "User deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }),
            @ApiResponse(responseCode ="404", description = "Entity not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while getting user", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @DeleteMapping("/{id}")
    @RequiredAdminUser
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestParam UUID id) {
        log.info("Received request to delete user {}", id);
        userService.deleteUser(id);
        log.info("User deleted: {}", id);
    }

    @PutMapping
    public UserResponse updatePassword(@RequestParam String oldPassword,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword, Principal principal) { // HttpServletRequest request

//        String authorization = request.getHeader("Authorization");
//        String token = authorization.substring("Bearer".length());

        String email = principal.getName();
        log.info("Received request to update password");
        UserResponse userResponse = userService.updatePassword(email, oldPassword, newPassword, confirmPassword);
        log.info("Password updated: {}", userResponse);
        return userResponse;
    }
}
