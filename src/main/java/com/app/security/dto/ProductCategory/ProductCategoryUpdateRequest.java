package com.app.security.dto.ProductCategory;

import jakarta.validation.constraints.NotBlank;

public class ProductCategoryUpdateRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
