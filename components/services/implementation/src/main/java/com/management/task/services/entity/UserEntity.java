package com.management.task.services.entity;

import com.management.task.management.services.Enums.Role;
import com.management.task.management.services.Enums.Status;
import com.management.task.management.services.constants.DatabaseConstants;
import com.management.task.management.services.requests.UserRequest;
import com.management.task.management.services.responses.UserResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = DatabaseConstants.USERS_TABLE_NAME, schema = DatabaseConstants.SCHEMA_NAME)
public class UserEntity {

    @Id
    @Column(name= "user_id", nullable = false)
    @UuidGenerator
    private UUID userId;

    @Column(name= "first_name", nullable = false)
    private String name;
    @Column(name= "last_name", nullable = false)
    private String surname;
    @Column(name= "email", nullable = false)
    private String email;
    @Column(name= "password", nullable = false)
    private String password;
    @Column(name= "verification_code")
    private String verifyCode;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity(UserRequest userRequest){
        userId = userRequest.getUserId();
        name = userRequest.getName();
        surname = userRequest.getSurname();
        email = userRequest.getEmail();
        password = userRequest.getPassword();
        verifyCode = userRequest.getVerifyCode();
        status = userRequest.getStatus();
        role = userRequest.getRole();
    }

    public UserRequest toUserRequest(){
        return new UserRequest(userId, name, surname, email, password, verifyCode, status, role);
    }

    public UserResponse toUserResponse(){
        return new UserResponse(userId, name, surname, email, password, verifyCode, status, role);
    }
}
