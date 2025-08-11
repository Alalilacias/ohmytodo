package com.ohmy.todo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    final private AuthenticationProvider authenticationProvider;

    private static final String[] PUBLIC_GET_ENDPOINTS = {
            // Pre-requisites for frontend
            "/favicon.ico",
            "/images/*",
            "/scripts/*",
            // REST endpoints
            "/api/users/all",
            "/api/todos",
            "/api/todos/*",
            // MVC endpoints
            "/index",
    };

    private static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/users",
            "/api/todos",
            "/api/auth/login",
            "/api/auth/logout"
    };

    private static final String[] PUBLIC_RESOURCES = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(PUBLIC_RESOURCES).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, ex) -> {
                            log.warn("Access denied at {}", request.getRequestURI());
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"status\":403,\"message\":\"Access denied\"}");
                        })
                        .authenticationEntryPoint((request, response, ex) -> {
                            log.warn("Unauthorized access attempt to {} | Reason: {}", request.getRequestURI(), ex.getMessage());
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"status\":401,\"message\":\"User is not authenticated\"}");
                        }))
                .authenticationProvider(authenticationProvider)
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("auth/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }
}
