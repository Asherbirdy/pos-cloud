package com.app.security.dto.MemberStoreAccess;

import com.app.security.enums.StoreRole;

public class MemberStoreAccessUpdateRequest {

    private StoreRole role;

    private Boolean isActive;

    public StoreRole getRole() {
        return role;
    }

    public void setRole(StoreRole role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
