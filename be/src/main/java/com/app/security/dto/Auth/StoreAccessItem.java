package com.app.security.dto.Auth;

public record StoreAccessItem(String storeId,
                              String storeName,
                              Boolean storeActive,
                              String role,
                              Boolean accessActive

) {
}
