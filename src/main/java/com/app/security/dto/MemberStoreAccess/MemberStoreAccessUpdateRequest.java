package com.app.security.dto.MemberStoreAccess;

import com.app.security.enums.StoreRole;

public class MemberStoreAccessUpdateRequest {

    private StoreRole role;

    private String status;

    public StoreRole getRole() {
        return role;
    }

    public void setRole(StoreRole role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
