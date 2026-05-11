package com.app.security.model;

import java.util.Date;

public class EmailVerificationCode {

    private String emailVerificationCodeId;
    private String email;
    private String codeHash;
    private Date expiresAt;
    private Integer attempts;
    private Boolean consumed;
    private Date createdAt;

    public String getEmailVerificationCodeId() {
        return emailVerificationCodeId;
    }

    public void setEmailVerificationCodeId(String emailVerificationCodeId) {
        this.emailVerificationCodeId = emailVerificationCodeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Boolean getConsumed() {
        return consumed;
    }

    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
