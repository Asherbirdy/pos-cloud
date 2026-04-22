package com.app.security.dao;

import com.app.security.model.Enterprise;

public interface EnterpriseDao {
    Enterprise getAllEnterprise();
    Enterprise editEnterpriseById(String id);

    Enterprise createEnterprise(Enterprise enterprise);
}
