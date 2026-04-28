package com.app.security.dto.ProductCategory;

import jakarta.validation.constraints.NotBlank;

public class ProductCategoryCreateRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
