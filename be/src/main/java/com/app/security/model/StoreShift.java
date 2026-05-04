package com.app.security.model;

import com.app.security.enums.ShiftStatus;

import java.time.LocalDate;
import java.util.Date;

public class StoreShift {
    private String storeShiftId;
    private String storeId;
    private String memberId;
    private LocalDate date;
    private ShiftStatus status;
    private Date openTime;
    private Date closeTime;
    private Date createdAt;
    private Date updatedAt;

    public String getStoreShiftId() {
        return storeShiftId;
    }

    public void setStoreShiftId(String storeShiftId) {
        this.storeShiftId = storeShiftId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ShiftStatus getStatus() {
        return status;
    }

    public void setStatus(ShiftStatus status) {
        this.status = status;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
