package com.app.security.service.impl;

import com.app.security.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
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
public class EnterpriseServiceImplTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("admin 登入 -> 訪問 [GET] /enterprise/ 應得 200")
    public void checkAdminRolePermit() throws Exception {
        mockMvc.perform(get("/enterprise/")
                .cookie(adminAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("user 登入 -> 訪問 [GET] /enterprise/ 應得 403")
    public void checkUserRolePermit() throws Exception {
        mockMvc.perform(get("/enterprise/")
                        .cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }

}