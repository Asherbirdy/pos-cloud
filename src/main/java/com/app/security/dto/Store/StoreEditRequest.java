package com.app.security.dto.Store;

import jakarta.validation.constraints.NotBlank;

public class StoreEditRequest {

    @NotBlank
    private String name;

    private Boolean isActive;

    private Integer running_devices_limit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getRunning_devices_limit() {
        return running_devices_limit;
    }

    public void setRunning_devices_limit(Integer running_devices_limit) {
        this.running_devices_limit = running_devices_limit;
    }
}
