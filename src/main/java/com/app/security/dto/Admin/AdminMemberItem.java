package com.app.security.dto.Admin;

import com.app.security.dto.Auth.StoreAccessItem;

import java.util.Date;
import java.util.List;

public class AdminMemberItem {

    private String memberId;
    private String name;
    private String email;
    private String role;
    private Date createdAt;
    private Date updatedAt;
    private List<StoreAccessItem> storeAccess;

    public AdminMemberItem(String memberId, String name, String email, String role,
                            Date createdAt, Date updatedAt, List<StoreAccessItem> storeAccess) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.storeAccess = storeAccess;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public List<StoreAccessItem> getStoreAccess() {
        return storeAccess;
    }
}
