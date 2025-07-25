package com.ohmy.todo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    final private AuthenticationProvider authenticationProvider;

    private static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/todos",
            "/api/todos/*"
    };

    private static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/users",
            "/api/todos",
            "/login",
            "/logout"
    };

    private static final String[] PUBLIC_RESOURCES = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    // Uncomment and modify as desired if you wish to have another frontend reach out to this backend
//    private static final List<String> CORS_ALLOWED_ORIGINS = List.of("http://localhost:3000");
//    private static final List<String> CORS_ALLOWED_METHODS = List.of("GET", "POST", "PATCH", "DELETE");
//    private static final List<String> CORS_ALLOWED_HEADERS = List.of("*");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(PUBLIC_RESOURCES).permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION")
                )
                .authenticationProvider(authenticationProvider)
                .build();
    }

//    The CORS configuration is added in case it was desired to test it with an external frontend.
//    It is included due to previous issues with frontends if not included
//    Uncomment and modify as desired if you wish to have another frontend reach out to this backend
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(CORS_ALLOWED_ORIGINS);
//        config.setAllowedMethods(CORS_ALLOWED_METHODS);
//        config.setAllowedHeaders(CORS_ALLOWED_HEADERS);
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}
