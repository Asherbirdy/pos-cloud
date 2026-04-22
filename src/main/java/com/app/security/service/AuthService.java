package com.app.security.service;

import com.app.security.dto.Auth.AuthLoginResponse;
import com.app.security.dto.Auth.AuthRegisterResponse;
import com.app.security.dto.Auth.LoginRequest;
import com.app.security.dto.Auth.RegisterRequest;

public interface AuthService {

    AuthRegisterResponse register(RegisterRequest registerRequest);

    AuthLoginResponse login(LoginRequest loginRequest);

    void logout();
}
