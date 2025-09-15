package com.javizs.store.games.controller.auth;

import com.javizs.store.games.dto.auth.*;
import com.javizs.store.games.entity.user.User;
import com.javizs.store.games.repository.user.UserRepository;
import com.javizs.store.games.security.jwt.JwtTokenProvider;
import com.javizs.store.games.security.user.CustomUserDetailsImpl;
import com.javizs.store.games.service.user.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            JwtAuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas", "message", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            JwtAuthResponse response = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor", "message", e.getMessage()));
        }

    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error","Falta header Authorization"));
        }

        String token = header.substring(7);
        String email = jwtTokenProvider.getUsernameFromToken(token);

        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        authService.logout(token, user.getId());

        return ResponseEntity.ok(Map.of("message","Sesión cerrada"));
    }



    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        authService.requestPasswordReset(forgotPasswordRequest);
        return ResponseEntity.ok(Map.of("message", "Código enviado si email existe"));

    }
    @PostMapping("/password/reset")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest resetPasswordRequest){
        authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(Map.of("message","contraseña actualizada"));
    }
}
