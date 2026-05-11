package com.app.security.service;

import com.app.security.dto.Auth.AuthLoginResponse;
import com.app.security.dto.Auth.AuthLoginStep1Response;
import com.app.security.dto.Auth.AuthRefreshTokenResponse;
import com.app.security.dto.Auth.AuthRegisterResponse;
import com.app.security.dto.Auth.LoginRequest;
import com.app.security.dto.Auth.LoginStep1Request;
import com.app.security.dto.Auth.RegisterRequest;

public interface AuthService {

    AuthRegisterResponse register(RegisterRequest registerRequest);

    AuthLoginStep1Response loginStep1(LoginStep1Request request);

    AuthLoginResponse login(LoginRequest loginRequest);

    void logout();

    AuthRegisterResponse registerAdmin(RegisterRequest registerRequest);

    AuthRefreshTokenResponse refreshToken(String authorizationHeader);
}
