package com.javizs.store.games.service.user;

import com.javizs.store.games.dto.auth.*;

public interface AuthService {
    JwtAuthResponse login(LoginRequest loginRequest);
    JwtAuthResponse register(RegisterRequest request);
    void requestPasswordReset(ForgotPasswordRequest forgotPasswordRequest);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    void logout(String token , Long userId);
}
