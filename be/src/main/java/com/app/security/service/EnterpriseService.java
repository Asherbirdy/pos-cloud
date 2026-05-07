package com.app.security.service;

import com.app.security.dto.Enterprise.EnterpriseWithStoresResponse;

import java.util.List;

public interface EnterpriseService {
    List<EnterpriseWithStoresResponse> getAll();

    void create(String name);

    void edit(String enterpriseId, String name);
}
