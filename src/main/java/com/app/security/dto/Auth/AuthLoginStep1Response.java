package com.app.security.dto.Auth;

/**
 * Step1 登入回應：
 * - 若該帳號不需要 OTP（如 user + 純 store_staff），requireOtp=false，並回傳完整 loginResponse。
 * - 若需要 OTP（admin / 任一店為 store_manager），requireOtp=true，loginResponse=null，
 *   後端會寄驗證碼到 email，前端再帶 code 打 /auth/login。
 */
public class AuthLoginStep1Response {

    private final boolean requireOtp;
    private final String email;
    private final AuthLoginResponse loginResponse;

    public AuthLoginStep1Response(boolean requireOtp, String email, AuthLoginResponse loginResponse) {
        this.requireOtp = requireOtp;
        this.email = email;
        this.loginResponse = loginResponse;
    }

    public boolean isRequireOtp() {
        return requireOtp;
    }

    public String getEmail() {
        return email;
    }

    public AuthLoginResponse getLoginResponse() {
        return loginResponse;
    }
}
