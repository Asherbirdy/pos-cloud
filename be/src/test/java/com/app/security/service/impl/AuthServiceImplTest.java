package com.app.security.service.impl;

import com.app.security.support.AuthTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServiceImplTest extends AuthTestSupport {

    /**
     * 驗證：USER 帳號（user@gmail.com，僅 STORE_STAFF）打 /auth/login/step1，
     * 應直接拿到 token（cookie），且不應觸發寄送 OTP。
     */
    @Test
    @DisplayName("loginStep1: staff 帳號直接拿 token，不寄 OTP")
    public void loginStep1_staffOnly_returnsTokenDirectly() throws Exception {
        clearInvocations(emailService);

        Map<String, String> body = Map.of(
                "email", "user@gmail.com",
                "password", "password"
        );

        mockMvc.perform(post("/auth/login/step1")
                        .header("X-Forwarded-For", randomIp())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requireOtp").value(false))
                .andExpect(jsonPath("$.loginResponse.tokenPair.accessToken").exists())
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"));

        verify(emailService, never()).sendVerificationCode(eq("user@gmail.com"), org.mockito.ArgumentMatchers.anyString());
    }

    /**
     * 驗證：ADMIN 帳號（role=admin）打 /auth/login/step1 → requireOtp=true 且寄送驗證碼；
     * 帶正確 code 打 /auth/login 應拿到 token cookie。
     */
    @Test
    @DisplayName("loginStep1 → login: admin 需 OTP，正確碼可登入")
    public void login_adminWithCorrectOtp_returnsToken() throws Exception {
        clearInvocations(emailService);

        Map<String, String> step1 = Map.of(
                "email", "admin@gmail.com",
                "password", "password"
        );

        mockMvc.perform(post("/auth/login/step1")
                        .header("X-Forwarded-For", randomIp())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(step1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requireOtp").value(true))
                .andExpect(jsonPath("$.loginResponse").doesNotExist());

        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, atLeastOnce()).sendVerificationCode(eq("admin@gmail.com"), codeCaptor.capture());
        String code = codeCaptor.getValue();

        Map<String, String> step2 = Map.of(
                "email", "admin@gmail.com",
                "password", "password",
                "code", code
        );

        mockMvc.perform(post("/auth/login")
                        .header("X-Forwarded-For", randomIp())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(step2)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"));
    }

    /**
     * 驗證：admin step1 拿到 OTP 後，帶錯誤 code 打 /auth/login，應回 400 OTP_INVALID。
     */
    @Test
    @DisplayName("login: admin 帶錯 OTP → 400 OTP_INVALID")
    public void login_adminWithWrongOtp_returnsBadRequest() throws Exception {
        clearInvocations(emailService);

        Map<String, String> step1 = Map.of(
                "email", "admin@gmail.com",
                "password", "password"
        );
        mockMvc.perform(post("/auth/login/step1")
                        .header("X-Forwarded-For", randomIp())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(step1)))
                .andExpect(status().isOk());

        verify(emailService, atLeastOnce()).sendVerificationCode(eq("admin@gmail.com"), org.mockito.ArgumentMatchers.anyString());

        Map<String, String> step2 = Map.of(
                "email", "admin@gmail.com",
                "password", "password",
                "code", "000000"
        );

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(step2)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // 訊息應包含 OTP_INVALID（GlobalExceptionHandler 會把 reason 帶出來）
        org.junit.jupiter.api.Assertions.assertTrue(
                result.getResponse().getContentAsString().contains("OTP_INVALID"),
                "expected response to mention OTP_INVALID");
    }

    /**
     * 驗證：帳密錯誤時 /auth/login/step1 應回 400，且不寄出 OTP。
     */
    @Test
    @DisplayName("loginStep1: 帳密錯誤 → 400 且不寄 OTP")
    public void loginStep1_wrongPassword_doesNotSendOtp() throws Exception {
        clearInvocations(emailService);

        Map<String, String> body = Map.of(
                "email", "admin@gmail.com",
                "password", "wrong-password"
        );

        mockMvc.perform(post("/auth/login/step1")
                        .header("X-Forwarded-For", randomIp())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());

        verify(emailService, never()).sendVerificationCode(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString());
    }
}
