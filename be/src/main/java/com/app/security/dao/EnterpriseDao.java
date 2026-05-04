package com.app.security.dao;

import com.app.security.model.Enterprise;

import java.util.List;

public interface EnterpriseDao {
    List<Enterprise> getAllEnterprise();

    void createEnterprise(String name);

    void editEnterpriseById(String enterpriseId, String name);
}
