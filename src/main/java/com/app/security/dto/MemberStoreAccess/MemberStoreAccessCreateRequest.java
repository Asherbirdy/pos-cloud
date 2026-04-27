package com.app.security.dto.MemberStoreAccess;

import com.app.security.enums.StoreRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MemberStoreAccessCreateRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String storeId;

    @NotNull
    private StoreRole role;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public StoreRole getRole() {
        return role;
    }

    public void setRole(StoreRole role) {
        this.role = role;
    }
}
