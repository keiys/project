package com.management.task.services.springJpa;

import com.management.task.services.exceptions.userexceptions.UserApiException;
import com.management.task.services.exceptions.userexceptions.UserBadRequestException;
import com.management.task.services.requests.LoginRequest;
import com.management.task.services.services.LoginService;
import com.management.task.services.entity.UserEntity;
import com.management.task.services.repository.UserRepository;
import com.management.task.services.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LoginSpringJpa implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String login(LoginRequest loginRequest){
        String token = null;
        try{
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), new ArrayList<>()));
            String email = authentication.getName();
            UserEntity userEntity = userRepository.getByEmail(email);
            userEntity.setPassword("");
            token = jwtUtil.createToken(userEntity);
        }catch (BadCredentialsException e){
            throw new UserBadRequestException("Invalid username or password");
        }catch (Exception e){
            throw new UserApiException("Problem durring getting token");
        }
        return token;
    }
}
