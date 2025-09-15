package com.javizs.store.games.security.config;

import com.javizs.store.games.repository.auth.BlacklistedTokenRepository;
import com.javizs.store.games.repository.user.UserRepository;
import com.javizs.store.games.security.jwt.JwtAuthenticationEntryPoint;
import com.javizs.store.games.security.jwt.JwtAuthenticationFilter;
import com.javizs.store.games.security.jwt.JwtTokenProvider;
import com.javizs.store.games.security.user.CustomUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsServiceImpl customUserDetailsService; // Inyectar directamente
    private final PasswordEncoder passwordEncoder;
    private final BlacklistedTokenRepository blacklistedTokenRepository;



    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter( ) {
        return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService, blacklistedTokenRepository );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }
}