package com.three.recipinglikeservicebe.global.config;

import com.three.recipinglikeservicebe.global.jwt.JwtUtil;
import com.three.recipinglikeservicebe.global.security.JwtOptionalFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtOptionalFilter jwtOptionalFilter = new JwtOptionalFilter(jwtUtil);

        http
//                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtOptionalFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}