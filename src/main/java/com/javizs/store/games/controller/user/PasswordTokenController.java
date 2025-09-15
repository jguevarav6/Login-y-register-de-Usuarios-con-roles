package com.javizs.store.games.controller.user;

import com.javizs.store.games.entity.user.PasswordResetToken;
import com.javizs.store.games.service.user.PasswordTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class PasswordTokenController {
    private final PasswordTokenService passwordTokenService;

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token){
        Optional<PasswordResetToken>passwordResetToken= passwordTokenService.validateToken(token);
        if (passwordResetToken.isPresent()){
            return ResponseEntity.ok("Token valido");

        }else{
            return ResponseEntity.badRequest().body("token inválido o ya usado");
        }
    }
}
