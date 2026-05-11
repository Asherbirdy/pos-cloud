package com.app.security.support;

import com.app.security.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AuthTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected EmailService emailService;

    protected Cookie adminAccessToken;
    protected Cookie userAccessToken;

    @BeforeAll
    void setUp() throws Exception {
        // admin 有 STORE_MANAGER + role=admin → 需要 OTP
        adminAccessToken = loginAndGetAccessToken("admin@gmail.com", "password", true);
        // user 只有 STORE_STAFF → step1 直接拿 token
        userAccessToken = loginAndGetAccessToken("user@gmail.com", "password", false);
    }

    protected Cookie loginAndGetAccessToken(String email, String password, boolean requireOtp) throws Exception {
        Map<String, String> step1Body = Map.of(
                "email", email,
                "password", password
        );

        clearInvocations(emailService);

        String ip = randomIp();
        MvcResult step1Result = mockMvc.perform(post("/auth/login/step1")
                        .header("X-Forwarded-For", ip)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(step1Body)))
                .andExpect(status().isOk())
                .andReturn();

        if (!requireOtp) {
            return step1Result.getResponse().getCookie("accessToken");
        }

        // 從 mocked EmailService 抓 6 碼 OTP
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, atLeastOnce()).sendVerificationCode(org.mockito.ArgumentMatchers.eq(email), codeCaptor.capture());
        String code = codeCaptor.getValue();

        Map<String, String> step2Body = Map.of(
                "email", email,
                "password", password,
                "code", code
        );

        MvcResult step2Result = mockMvc.perform(post("/auth/login")
                        .header("X-Forwarded-For", ip)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(step2Body)))
                .andExpect(status().isOk())
                .andReturn();

        return step2Result.getResponse().getCookie("accessToken");
    }

    private static final java.util.concurrent.atomic.AtomicInteger ipCounter = new java.util.concurrent.atomic.AtomicInteger(0);

    protected static String randomIp() {
        int n = ipCounter.incrementAndGet();
        // 10.<a>.<b>.<c> — 不會與真實 client IP 衝突的私有網段
        return "10." + ((n >> 16) & 0xff) + "." + ((n >> 8) & 0xff) + "." + (n & 0xff);
    }
}
