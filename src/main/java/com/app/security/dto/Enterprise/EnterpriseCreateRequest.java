package com.app.security.dto.Enterprise;

import jakarta.validation.constraints.NotBlank;

public class EnterpriseCreateRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
