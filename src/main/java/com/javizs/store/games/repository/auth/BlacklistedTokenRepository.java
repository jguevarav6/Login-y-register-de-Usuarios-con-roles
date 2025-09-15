package com.javizs.store.games.repository.auth;

import com.javizs.store.games.entity.auth.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface BlacklistedTokenRepository  extends JpaRepository<BlackListedToken, Long> {
    boolean existsByTokenAndExpiresAtAfter(String token, Instant now);

}
