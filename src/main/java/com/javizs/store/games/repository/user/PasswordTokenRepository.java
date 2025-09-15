package com.javizs.store.games.repository.user;

import com.javizs.store.games.entity.user.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository  extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenAndUsedFalse(String token);
}
