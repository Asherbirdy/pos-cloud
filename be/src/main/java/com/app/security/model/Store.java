package com.app.security.model;

import java.util.Date;

public class Store {
    private String storeId;
    private String name;
    private Boolean isActive;
    private Integer runningDevicesLimit;
    private Date createdAt;
    private Date updatedAt;

    public Integer getRunningDevicesLimit() {
        return runningDevicesLimit;
    }

    public void setRunningDevicesLimit(Integer runningDevicesLimit) {
        this.runningDevicesLimit = runningDevicesLimit;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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
