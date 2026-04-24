package com.app.security.dto.MemberStoreAccess;

import jakarta.validation.constraints.NotBlank;

public class MemberStoreAccessCreateRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String storeId;

    @NotBlank
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
