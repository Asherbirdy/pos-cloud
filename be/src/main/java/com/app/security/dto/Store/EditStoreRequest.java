package com.app.security.dto.Store;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class EditStoreRequest {

    @NotBlank
    private String name;

    private List<String> userAgents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUserAgents() {
        return userAgents;
    }

    public void setUserAgents(List<String> userAgents) {
        this.userAgents = userAgents;
    }
}
