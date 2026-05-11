package com.app.security.dto.Auth;

import java.util.List;

public class AuthLoginResponse {
    private final String name;
    private final String memberId;
    private final String role;
    private final List<StoreAccessItem> storeAccess;
    private final TokenPair tokenPair;

    public AuthLoginResponse(String name, String memberId, String role, List<StoreAccessItem> storeAccess, TokenPair tokenPair) {
        this.name = name;
        this.memberId = memberId;
        this.role = role;
        this.storeAccess = storeAccess;
        this.tokenPair = tokenPair;
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

    public TokenPair getTokenPair(){return tokenPair ;}
}
