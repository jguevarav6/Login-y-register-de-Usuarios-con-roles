package com.javizs.store.games.service.user;

import com.javizs.store.games.entity.user.PasswordResetToken;
import com.javizs.store.games.repository.user.PasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordTokenService {
    private final PasswordTokenRepository tokenRepository;
    public Optional<PasswordResetToken> validateToken(String token){
        return tokenRepository.findByTokenAndUsedFalse(token);
    }
}
