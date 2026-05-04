package com.app.security.dto.StoreProductCategory;

import jakarta.validation.constraints.NotBlank;

public class StoreProductCategoryCreateRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
