package com.app.security.dto.Auth;

import java.util.List;

public class AuthLoginResponse {
    private final String name;
    private final String memberId;
    private final String role;
    private final List<StoreAccessItem> storeAccess;

    public AuthLoginResponse(String name, String memberId, String role, List<StoreAccessItem> storeAccess) {
        this.name = name;
        this.memberId = memberId;
        this.role = role;
        this.storeAccess = storeAccess;
    }

    public String getName() {
        return name;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getRole() {
        return role;
    }

    public List<StoreAccessItem> getStoreAccess() {
        return storeAccess;
    }
}
