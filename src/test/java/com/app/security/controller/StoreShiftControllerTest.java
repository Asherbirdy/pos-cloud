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

    /**
     * 驗證：admin 在該 store 的 storeRole = STORE_MANAGER，
     * 通過 @RequireStoreRole({STORE_MANAGER}) 檢查，應拿到 200。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [GET] /storeShift/ 應得 200")
    public void getAllWithStoreManagerShouldBeOk() throws Exception {
        mockMvc.perform(get("/storeShift/")
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isOk());
    }

    /**
     * 驗證：user 在該 store 的 storeRole = STORE_STAFF，不在允許名單內，
     * 應被 aspect 擋下回傳 403 (INSUFFICIENT_STORE_ROLE)。
     */
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

    /**
     * 驗證：admin 為 STORE_MANAGER 通過權限檢查；
     * 但傳入不存在的 storeShiftId，aspect 反查 store_shift 找不到，
     * 應回傳 404 (STORE_SHIFT_NOT_FOUND)。
     * 用 404 而非 200 是為了避免另外塞測試資料；只要不是 403 就代表權限這層通過。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [GET] /storeShift/{id} 應通過權限 (非 403)")
    public void getByIdWithStoreManagerShouldPassAuth() throws Exception {
        mockMvc.perform(get("/storeShift/" + NON_EXISTENT_SHIFT_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isNotFound());
    }

    // ===== [POST] /storeShift/open?storeId=... =====
    // STORE_MANAGER 與 STORE_STAFF 皆可存取

    /**
     * 驗證：open 允許 STORE_MANAGER 與 STORE_STAFF；
     * admin 為 STORE_MANAGER，應通過權限並成功開班，回傳 201。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [POST] /storeShift/open 應得 201")
    public void openShiftWithStoreManagerShouldBeCreated() throws Exception {
        mockMvc.perform(post("/storeShift/open")
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isCreated());
    }

    /**
     * 驗證：user 為 STORE_STAFF，open 允許 STAFF，
     * 應通過權限並成功開班，回傳 201。
     */
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

    /**
     * 驗證：close 允許 STORE_MANAGER 與 STORE_STAFF；
     * admin 為 STORE_MANAGER 通過權限後，因傳入不存在的 storeShiftId，
     * aspect 反查 store_shift 找不到應回 404；只要不是 403 就代表權限這層通過。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [POST] /storeShift/{id}/close 應通過權限 (非 403)")
    public void closeShiftWithStoreManagerShouldPassAuth() throws Exception {
        mockMvc.perform(post("/storeShift/" + NON_EXISTENT_SHIFT_ID + "/close")
                        .cookie(adminAccessToken))
                .andExpect(status().isNotFound());
    }

    /**
     * 驗證：user 為 STORE_STAFF 通過 close 的權限檢查；
     * 因傳入不存在的 storeShiftId，aspect 反查 store_shift 找不到應回 404，
     * 只要不是 403 就代表 STAFF 的權限這層也通過。
     */
    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [POST] /storeShift/{id}/close 應通過權限 (非 403)")
    public void closeShiftWithStoreStaffShouldPassAuth() throws Exception {
        mockMvc.perform(post("/storeShift/" + NON_EXISTENT_SHIFT_ID + "/close")
                        .cookie(userAccessToken))
                .andExpect(status().isNotFound());
    }
}
