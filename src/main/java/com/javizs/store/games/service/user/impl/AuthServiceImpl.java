package com.javizs.store.games.service.user.impl;

import com.javizs.store.games.dto.auth.*;
import com.javizs.store.games.entity.auth.BlackListedToken;
import com.javizs.store.games.entity.user.PasswordResetToken;
import com.javizs.store.games.entity.user.Role;
import com.javizs.store.games.entity.user.User;
import com.javizs.store.games.repository.auth.BlacklistedTokenRepository;
import com.javizs.store.games.repository.user.PasswordTokenRepository;
import com.javizs.store.games.repository.user.UserRepository;
import com.javizs.store.games.security.jwt.JwtTokenProvider;
import com.javizs.store.games.service.email.EmailService;
import com.javizs.store.games.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final PasswordTokenRepository tokenRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    public JwtAuthResponse login(LoginRequest loginRequest) {
        if (!StringUtils.hasText(loginRequest.getEmail()) || !StringUtils.hasText(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Email y contraseña son requeridos para login");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return JwtAuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public JwtAuthResponse register(RegisterRequest registerRequest) {
        if (!StringUtils.hasText(registerRequest.getEmail()) || !StringUtils.hasText(registerRequest.getPassword())) {
            throw new IllegalArgumentException("Email y contraseña son requeridos para registro");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setRol(Role.CUSTOMER);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateTokenFromUsername(user.getEmail());

        return JwtAuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void requestPasswordReset(ForgotPasswordRequest forgotPasswordRequest) {
        var user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese email"));

        var token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(generateSixDigits());
        token.setExpiresAt(Instant.now().plusSeconds(30 * 60));
        tokenRepository.save(token);

        var body = """
                Hola %s,
                Tu código para restablecer la contraseña es :%s 
                Este código expira en 30 minutos.
                Si no solicitaste esto, cambia tu contraseña inmediatamente.
                """.formatted(user.getName(), token.getToken());

        emailService.send(user.getEmail(), "Código de restablecimiento", body);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        var token = tokenRepository.findByTokenAndUsedFalse(resetPasswordRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Código inválido"));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Código expirado");
        }

        var user = token.getUser();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);

        token.setUsed(true);
        tokenRepository.save(token);
    }

    private String generateSixDigits() {
        int n = (int) (Math.random() * 1_000_000);
        return String.format("%06d", n);
    }
    public void logout(String token, Long userId){
        Instant exp = jwtTokenProvider.getExpirationDate(token).toInstant();
        BlackListedToken blackListedToken= new BlackListedToken();
        blackListedToken.setToken(token);
        blackListedToken.setUserId(userId);
        blackListedToken.setExpiresAt(exp);
        blackListedToken.setCreatedAt(Instant.now());
        blacklistedTokenRepository.save(blackListedToken);
    }
}
