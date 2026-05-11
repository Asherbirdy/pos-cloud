package com.app.security.dto.MemberStoreAccess;

import java.util.Date;

public class StoreMemberAccessItem {

    private String memberStoreAccessId;
    private String memberId;
    private String memberName;
    private String memberEmail;
    private String role;
    private Boolean isActive;
    private Date createdAt;

    public StoreMemberAccessItem(String memberStoreAccessId, String memberId, String memberName,
                                  String memberEmail, String role, Boolean isActive, Date createdAt) {
        this.memberStoreAccessId = memberStoreAccessId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public String getMemberStoreAccessId() {
        return memberStoreAccessId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public String getRole() {
        return role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
