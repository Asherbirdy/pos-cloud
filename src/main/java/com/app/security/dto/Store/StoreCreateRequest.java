package com.app.security.dto.Store;

import jakarta.validation.constraints.NotBlank;

public class StoreCreateRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
