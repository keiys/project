package com.management.task.services.security;

import com.management.task.services.exceptions.userexceptions.UserApiException;
import com.management.task.services.exceptions.userexceptions.UserNotFoundException;
import com.management.task.services.entity.UserEntity;
import com.management.task.services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity;

        try {
            userEntity = userRepository.getByEmail(email);
        }
        catch (Exception e) {
            throw new UserApiException("Error while load user by username: " + email, e);
        }
        if(userEntity == null) {
            throw new UserNotFoundException("User not found with username: " + email);
        }

        return User.builder()
                .username(email)
                .password(userEntity.getPassword())
                .roles(String.valueOf(userEntity.getRole()))
                .authorities(new ArrayList<>())
                .build();
    }
}
