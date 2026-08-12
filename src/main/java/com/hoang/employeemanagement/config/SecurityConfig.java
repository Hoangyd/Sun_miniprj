package com.hoang.employeemanagement.config;

import com.hoang.employeemanagement.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/employees/report/count").permitAll()
                        .requestMatchers("/employees/statistics").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/web/**").permitAll()
                        .requestMatchers("GET", "/employees").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("GET", "/employees/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("POST", "/employees").hasRole("ADMIN")
                        .requestMatchers("PUT", "/employees/**").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/employees/**").hasRole("ADMIN")
                        .requestMatchers("GET", "/departments").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("POST", "/departments").hasRole("ADMIN")
                        .requestMatchers("PUT", "/departments/**").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/departments/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
