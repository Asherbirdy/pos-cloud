package com.app.security.service.impl;

import com.app.security.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthServiceImplTest extends BaseTest {

    @Test
    public void login() throws Exception {
        Map<String, String> loginRequest = Map.of(
                "email", "admin@gmail.com",
                "password", "password"
        );

        RequestBuilder requestBuilder = post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().exists("refreshToken"));
    }

}
