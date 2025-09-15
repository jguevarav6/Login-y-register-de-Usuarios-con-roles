package com.javizs.store.games.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String name;
    private String email;
}
