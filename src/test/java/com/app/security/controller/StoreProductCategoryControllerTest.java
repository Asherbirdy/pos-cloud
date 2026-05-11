package com.app.security.controller;

import com.app.security.support.AuthTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreProductCategoryControllerTest extends AuthTestSupport {

    private static final String STORE_ID = "acceeb7d-59da-4edf-b543-514d728d46c8";
    private static final String NON_EXISTENT_CATEGORY_ID = "00000000-0000-0000-0000-000000000000";
    private static final String EXISTING_CATEGORY_ID = "c1d2e3f4-0001-4000-8000-000000000001";
    private static final String BASE_URL = "/product-category";

    // ===== [GET] /product-category/?storeId=... =====
    // 僅 STORE_MANAGER 可存取

    /**
     * 驗證：admin 在該 store 的 storeRole = STORE_MANAGER，
     * 通過 @RequireStoreRole({STORE_MANAGER}) 檢查，應拿到 200。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [GET] /product-category/ 應得 200")
    public void getAllWithStoreManagerShouldBeOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "/")
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isOk());
    }

    /**
     * 驗證：user 在該 store 的 storeRole = STORE_STAFF，不在允許名單內，
     * 應被 aspect 擋下回傳 403 (INSUFFICIENT_STORE_ROLE)。
     */
    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [GET] /product-category/ 應得 403")
    public void getAllWithStoreStaffShouldBeForbidden() throws Exception {
        mockMvc.perform(get(BASE_URL + "/")
                        .param("storeId", STORE_ID)
                        .cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }

    // ===== [GET] /product-category/{productCategoryId}?storeId=... =====
    // 僅 STORE_MANAGER 可存取

    /**
     * 驗證：admin 為 STORE_MANAGER 通過權限檢查；
     * 但傳入不存在的 productCategoryId，service 反查找不到，
     * 應回傳 404 (STORE_PRODUCT_CATEGORY_NOT_FOUND)。
     * 用 404 而非 200 是為了避免另外塞測試資料；只要不是 403 就代表權限這層通過。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [GET] /product-category/{id} 應通過權限 (非 403)")
    public void getByIdWithStoreManagerShouldPassAuth() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + NON_EXISTENT_CATEGORY_ID)
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isNotFound());
    }

    /**
     * 驗證：user 為 STORE_STAFF，不在允許名單內。
     * 用真實存在的 productCategoryId，讓 aspect 反查 storeId 成功後進入角色檢查，
     * 應被擋下回傳 403 (INSUFFICIENT_STORE_ROLE)。
     */
    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [GET] /product-category/{id} 應得 403")
    public void getByIdWithStoreStaffShouldBeForbidden() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + EXISTING_CATEGORY_ID)
                        .param("storeId", STORE_ID)
                        .cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }

    // ===== [POST] /product-category/?storeId=... =====
    // 僅 STORE_MANAGER 可存取

    /**
     * 驗證：admin 為 STORE_MANAGER 通過權限後，新增成功應回傳 201。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [POST] /product-category/ 應得 201")
    public void createWithStoreManagerShouldBeCreated() throws Exception {
        mockMvc.perform(post(BASE_URL + "/")
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"飲料\"}"))
                .andExpect(status().isCreated());
    }

    /**
     * 驗證：user 為 STORE_STAFF，不在允許名單內，
     * 應被 aspect 擋下回傳 403 (INSUFFICIENT_STORE_ROLE)。
     */
    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [POST] /product-category/ 應得 403")
    public void createWithStoreStaffShouldBeForbidden() throws Exception {
        mockMvc.perform(post(BASE_URL + "/")
                        .param("storeId", STORE_ID)
                        .cookie(userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"飲料\"}"))
                .andExpect(status().isForbidden());
    }

    // ===== [PATCH] /product-category/{productCategoryId}?storeId=... =====
    // 僅 STORE_MANAGER 可存取

    /**
     * 驗證：admin 為 STORE_MANAGER 通過權限後，因傳入不存在的 productCategoryId，
     * service 反查找不到應回 404；只要不是 403 就代表權限這層通過。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [PATCH] /product-category/{id} 應通過權限 (非 403)")
    public void updateWithStoreManagerShouldPassAuth() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/" + NON_EXISTENT_CATEGORY_ID)
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"新名稱\"}"))
                .andExpect(status().isNotFound());
    }

    /**
     * 驗證：user 為 STORE_STAFF，不在允許名單內。
     * 用真實存在的 productCategoryId，讓 aspect 反查 storeId 成功後進入角色檢查，
     * 應被擋下回傳 403 (INSUFFICIENT_STORE_ROLE)。
     */
    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [PATCH] /product-category/{id} 應得 403")
    public void updateWithStoreStaffShouldBeForbidden() throws Exception {
        mockMvc.perform(patch(BASE_URL + "/" + EXISTING_CATEGORY_ID)
                        .param("storeId", STORE_ID)
                        .cookie(userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"新名稱\"}"))
                .andExpect(status().isForbidden());
    }

    // ===== [DELETE] /product-category/{productCategoryId}?storeId=... =====
    // 僅 STORE_MANAGER 可存取

    /**
     * 驗證：admin 為 STORE_MANAGER 通過權限後，因傳入不存在的 productCategoryId，
     * service 反查找不到應回 404；只要不是 403 就代表權限這層通過。
     */
    @Test
    @DisplayName("admin (STORE_MANAGER) 登入 -> [DELETE] /product-category/{id} 應通過權限 (非 403)")
    public void deleteWithStoreManagerShouldPassAuth() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + NON_EXISTENT_CATEGORY_ID)
                        .param("storeId", STORE_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isNotFound());
    }

    /**
     * 驗證：user 為 STORE_STAFF，不在允許名單內。
     * 用真實存在的 productCategoryId，讓 aspect 反查 storeId 成功後進入角色檢查，
     * 應被擋下回傳 403 (INSUFFICIENT_STORE_ROLE)。
     */
    @Test
    @DisplayName("user (STORE_STAFF) 登入 -> [DELETE] /product-category/{id} 應得 403")
    public void deleteWithStoreStaffShouldBeForbidden() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + EXISTING_CATEGORY_ID)
                        .param("storeId", STORE_ID)
                        .cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }
}
