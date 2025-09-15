package com.javizs.store.games.entity.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity @Table(name = "jwt_blacklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackListedToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048 , nullable = false , unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Instant createdAt;
}
