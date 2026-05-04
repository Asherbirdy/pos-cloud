package com.app.security.controller;

import com.app.security.dto.MemberStoreAccess.MemberStoreAccessCreateRequest;
import com.app.security.dto.MemberStoreAccess.MemberStoreAccessUpdateRequest;
import com.app.security.enums.StoreRole;
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
public class MemberStoreAccessControllerTest extends AuthTestSupport {

    private static final String STORE_ID = "acceeb7d-59da-4edf-b543-514d728d46c8";
    private static final String MEMBER_ID = "92c14284-b0a8-4e2b-8dce-1f6e918b8374";

    @Test
    @DisplayName("user 登入 -> [POST] /member-store-access/ 應得 403")
    public void createWithUserShouldBeForbidden() throws Exception {
        MemberStoreAccessCreateRequest request = new MemberStoreAccessCreateRequest();
        request.setMemberId(MEMBER_ID);
        request.setStoreId(STORE_ID);
        request.setRole(StoreRole.STORE_STAFF);

        mockMvc.perform(post("/member-store-access/")
                        .cookie(userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("user 登入 -> [GET] /member-store-access/{storeId} 應得 403")
    public void getByStoreIdWithUserShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/member-store-access/" + STORE_ID)
                        .cookie(userAccessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("user 登入 -> [PATCH] /member-store-access/{memberStoreAccessId} 應得 403")
    public void updateWithUserShouldBeForbidden() throws Exception {
        MemberStoreAccessUpdateRequest request = new MemberStoreAccessUpdateRequest();
        request.setRole(StoreRole.STORE_MANAGER);
        request.setIsActive(true);

        mockMvc.perform(patch("/member-store-access/some-id")
                        .cookie(userAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("admin 登入 -> [GET] /member-store-access/{storeId} 應得 200")
    public void getByStoreIdWithAdminShouldBeOk() throws Exception {
        mockMvc.perform(get("/member-store-access/" + STORE_ID)
                        .cookie(adminAccessToken))
                .andExpect(status().isOk());
    }
}
