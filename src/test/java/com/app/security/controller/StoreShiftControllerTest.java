package com.app.security.controller;

import com.app.security.support.AuthTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreShiftControllerTest extends AuthTestSupport {

    private static final String STORE_ID = "acceeb7d-59da-4edf-b543-514d728d46c8";
    private static final String NON_EXISTENT_SHIFT_ID = "00000000-0000-0000-0000-000000000000";

    // ===== [GET] /storeShift/?storeId=... =====
    // 僅 STORE_MANAGER 可存取

    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [GET] /storeShift/ 應得 200")
    public void getAllWithStoreManagerShouldBeOk() throws Exception {
        mockMvc.perform(get("/storeShift/")
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [GET] /storeShift/ 應得 403")
    public void getAllWithStoreStaffShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/storeShift/")
                        .param("storeId", STORE_ID)
                        .cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }

    // ===== [GET] /storeShift/{storeShiftId} =====
    // 僅 STORE_MANAGER 可存取

    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [GET] /storeShift/{id} 應通過權限 (非 403)")
    public void getByIdWithStoreManagerShouldPassAuth() throws Exception {
        mockMvc.perform(get("/storeShift/" + NON_EXISTENT_SHIFT_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [GET] /storeShift/{id} 應得 403")
    public void getByIdWithStoreStaffShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/storeShift/" + NON_EXISTENT_SHIFT_ID)
                        .cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }

    // ===== [POST] /storeShift/open?storeId=... =====
    // STORE_MANAGER 與 STORE_STAFF 皆可存取

    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [POST] /storeShift/open 應得 201")
    public void openShiftWithStoreManagerShouldBeCreated() throws Exception {
        mockMvc.perform(post("/storeShift/open")
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [POST] /storeShift/open 應得 201")
    public void openShiftWithStoreStaffShouldBeCreated() throws Exception {
        mockMvc.perform(post("/storeShift/open")
                        .param("storeId", STORE_ID)
                        .cookie(userAccessToken))
                .andExpect(status().isCreated());
    }

    // ===== [POST] /storeShift/{storeShiftId}/close =====
    // STORE_MANAGER 與 STORE_STAFF 皆可存取

    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [POST] /storeShift/{id}/close 應通過權限 (非 403)")
    public void closeShiftWithStoreManagerShouldPassAuth() throws Exception {
        mockMvc.perform(post("/storeShift/" + NON_EXISTENT_SHIFT_ID + "/close")
                        .cookie(adminAccessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [POST] /storeShift/{id}/close 應通過權限 (非 403)")
    public void closeShiftWithStoreStaffShouldPassAuth() throws Exception {
        mockMvc.perform(post("/storeShift/" + NON_EXISTENT_SHIFT_ID + "/close")
                        .cookie(userAccessToken))
                .andExpect(status().isNotFound());
    }
}
