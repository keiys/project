package com.management.task.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetails customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

//    @Autowired
//    private JwtAuthenticationEntrypoint entrypoint;
//
//    @Autowired
//    private ThreadContextInterceptor interceptor;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = security.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.POST, "/api/v1/users")
                .permitAll());

        security.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c.requestMatchers("/api/*/*/openapi/**", "/auth")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return security.build();

    }


}
