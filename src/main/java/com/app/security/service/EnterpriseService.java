package com.app.security.service;

import com.app.security.model.Enterprise;

import java.util.List;

public interface EnterpriseService {
    List<Enterprise> getAll();

    void create(String name);

    void edit(String enterpriseId, String name);
}
