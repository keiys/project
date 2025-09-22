package com.management.task.services.entity;

import com.management.task.services.Enums.Role;
import com.management.task.services.Enums.Status;
import com.management.task.services.constants.DatabaseConstants;
import com.management.task.services.requests.UserRequest;
import com.management.task.services.responses.UserResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
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

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TaskEntity> createdTasks;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TaskEntity> assignedTasks;

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

    public UserEntity(UserResponse userResponse){
        userId = userResponse.userId();
        name = userResponse.name();
        surname = userResponse.surname();
        email = userResponse.email();
        password = userResponse.password();
        verifyCode = userResponse.verifyCode();
        status = userResponse.status();
        role = userResponse.role();
    }

    public UserRequest toUserRequest(){
        return new UserRequest(userId, name, surname, email, password, verifyCode, status, role);
    }

    public UserResponse toUserResponse(){
        return new UserResponse(userId, name, surname, email, password, verifyCode, status, role);
    }
}
