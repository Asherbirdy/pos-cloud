package com.app.security.model;

import com.app.security.enums.StoreRole;

import java.util.Date;

public class MemberStoreAccess {

    private String memberStoreAccessId;
    private String memberId;
    private String enterpriseId;
    private String storeId;
    private StoreRole role;
    private Boolean isActive;
    private Date createdAt;

    public String getMemberStoreAccessId() {
        return memberStoreAccessId;
    }

    public void setMemberStoreAccessId(String memberStoreAccessId) {
        this.memberStoreAccessId = memberStoreAccessId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
