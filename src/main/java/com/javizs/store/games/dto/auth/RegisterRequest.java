package com.javizs.store.games.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name ;
    private String email;
    private String password;
    private String phone;
}
