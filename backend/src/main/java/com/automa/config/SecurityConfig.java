package com.automa.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.automa.dto.ErrorResponse;
import com.automa.filter.JwtAuthenticationFilter;
import com.automa.services.implementation.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final CustomUserDetailsService userDetailsService;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(CustomUserDetailsService userDetailsService,
                        JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.userDetailsService = userDetailsService;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                return http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**",
                                                                "/swagger-ui.html")
                                                .permitAll()
                                                .requestMatchers("/", "/api/auth/**").permitAll()
                                                .anyRequest().authenticated())
                                .userDetailsService(userDetailsService)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(e -> e
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        response.setContentType("application/json");
                                                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                                        ErrorResponse errorResponse = new ErrorResponse(
                                                                        "Unauthorized",
                                                                        Arrays.asList("You need to authenticate to access this resource."));
                                                        response.getWriter().write(new ObjectMapper()
                                                                        .writeValueAsString(errorResponse));
                                                })
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        response.setContentType("application/json");
                                                        response.setStatus(HttpStatus.FORBIDDEN.value());
                                                        ErrorResponse errorResponse = new ErrorResponse(
                                                                        "Access Denied",
                                                                        Arrays.asList("You do not have permission to access this resource."));
                                                        response.getWriter().write(new ObjectMapper()
                                                                        .writeValueAsString(errorResponse));
                                                }))
                                .build();

        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
                return configuration.getAuthenticationManager();
        }
}