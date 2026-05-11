package com.app.security.dto.Auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    /**
     * Email OTP code from /auth/login/step1.
     * 對於需要 OTP 的角色（admin / 任一店為 store_manager）必填；
     * 不需要 OTP 的帳號直接走 /auth/login/step1 拿 token，不會走到這支。
     */
    @NotBlank
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
