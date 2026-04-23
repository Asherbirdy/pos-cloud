package com.app.security.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    public void checkRolePermit() throws Exception {
        // admin 登入 -> 訪問 /enterprise/ 應得 200
        Cookie adminAccessToken = loginAndGetAccessToken("admin@gmail.com", "password");

        mockMvc.perform(get("/enterprise/").cookie(adminAccessToken))
                .andExpect(status().isOk());

        // user 登入 -> 訪問 /enterprise/ 應得 403
        Cookie userAccessToken = loginAndGetAccessToken("user@gmail.com", "password");

        mockMvc.perform(get("/enterprise/").cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }

    private Cookie loginAndGetAccessToken(String email, String password) throws Exception {
        Map<String, String> loginRequest = Map.of(
                "email", email,
                "password", password
        );

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getCookie("accessToken");
    }
}
