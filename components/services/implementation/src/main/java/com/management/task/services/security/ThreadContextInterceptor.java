package com.management.task.services.security;

import com.management.task.UserContext;
import com.management.task.services.Enums.Role;
import com.management.task.services.entity.UserEntity;
import com.management.task.services.exceptions.userexceptions.UserApiException;
import com.management.task.services.exceptions.userexceptions.UserForbiddenException;
import com.management.task.services.exceptions.userexceptions.UserUnauthorizedException;
import com.management.task.services.repository.UserRepository;
import com.management.task.services.responses.UserResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ThreadContextInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public ThreadContextInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod handlerMethod)){
            return true;
        }

        NoAuthenticationRequired noAuthenticationRequired = handlerMethod.getMethodAnnotation(NoAuthenticationRequired.class);
        if (noAuthenticationRequired != null) {
            return true;
        }


        String token = jwtUtil.resolveToken(request);
        if(token == null){
            throw new UserUnauthorizedException("Token is missing");
        }

        Claims claims = jwtUtil.resolveClaims(request);
        if(claims != null && jwtUtil.validateClaims(claims)){
            UserResponse user = getUser(claims.getSubject());
            UserContext.setCurrentUser(user);
            Role role = Enum.valueOf(Role.class,(String) claims.get("role"));
            RequiredAdminUser methodAnnotation = handlerMethod.getMethodAnnotation(RequiredAdminUser.class);
            if (methodAnnotation != null) {
                if (!role.equals(Role.ADMIN)){
                    throw new UserForbiddenException("You are not allowed to perform this action");
                }
            }
        }
        return true;
    }

    private UserResponse getUser(String email){
        UserEntity user;
        try{
            user = userRepository.getByEmail(email);
        }catch (Exception e){
            throw new SecurityException("Required login action");
        }
        if (user == null){
            throw new SecurityException("Required login action");
        }
        return user.toUserResponse();
    }


}
