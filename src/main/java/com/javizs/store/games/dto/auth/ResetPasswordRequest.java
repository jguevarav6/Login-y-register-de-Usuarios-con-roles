package com.javizs.store.games.dto.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String newPassword;

}
