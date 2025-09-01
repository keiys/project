package com.management.task.services.controller;


import com.management.task.management.services.requests.LoginRequest;
import com.management.task.management.services.services.LoginService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public String auth(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

}
