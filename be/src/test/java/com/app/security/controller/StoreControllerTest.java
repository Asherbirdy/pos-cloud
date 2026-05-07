package com.app.security.controller;

import com.app.security.dto.Store.StoreCreateRequest;
import com.app.security.dto.Store.StoreEditRequest;
import com.app.security.support.AuthTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerTest extends AuthTestSupport {

    private static final String STORE_ID = "acceeb7d-59da-4edf-b543-514d728d46c8";

    @Test
    @DisplayName("user 登入 -> [GET] /store/ 應得 403")
    public void getAllWithUserShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/store/")
                        .cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("user 登入 -> [POST] /store/ 應得 403")
    public void createWithUserShouldBeForbidden() throws Exception {
        StoreCreateRequest request = new StoreCreateRequest();
        request.setName("新門市");

        mockMvc.perform(post("/store/")
                        .cookie(userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("user 登入 -> [PATCH] /store/{store_id} 應得 403")
    public void editWithUserShouldBeForbidden() throws Exception {
        StoreEditRequest request = new StoreEditRequest();
        request.setName("改名門市");
        request.setIsActive(true);
        request.setRunning_devices_limit(5);

        mockMvc.perform(patch("/store/" + STORE_ID)
                        .cookie(userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("admin 登入 -> [GET] /store/ 應得 200")
    public void getAllWithAdminShouldBeOk() throws Exception {
        mockMvc.perform(get("/store/")
                        .cookie(adminAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("admin 登入 -> [POST] /store/ 應得 201")
    public void createWithAdminShouldBeCreated() throws Exception {
        StoreCreateRequest request = new StoreCreateRequest();
        request.setName("新門市");

        mockMvc.perform(post("/store/")
                        .cookie(adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("admin 登入 -> [PATCH] /store/{store_id} 應得 200")
    public void editWithAdminShouldBeOk() throws Exception {
        StoreEditRequest request = new StoreEditRequest();
        request.setName("改名門市");
        request.setIsActive(true);
        request.setRunning_devices_limit(5);

        mockMvc.perform(patch("/store/" + STORE_ID)
                        .cookie(adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
