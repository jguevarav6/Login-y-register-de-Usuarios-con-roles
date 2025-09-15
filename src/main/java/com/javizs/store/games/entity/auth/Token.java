package com.javizs.store.games.entity.auth;

import com.javizs.store.games.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String token;
     private boolean revoked;
     private boolean expired;

     @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
