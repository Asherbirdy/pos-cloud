package com.app.security.service.impl;

import com.app.security.dao.EnterpriseDao;
import com.app.security.model.Enterprise;
import com.app.security.service.EnterpriseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseDao enterpriseDao;

    public EnterpriseServiceImpl(EnterpriseDao enterpriseDao) {
        this.enterpriseDao = enterpriseDao;
    }

    @Override
    public List<Enterprise> getAll() {
        return enterpriseDao.getAllEnterprise();
    }

    @Override
    public void create(String name) {
        enterpriseDao.createEnterprise(name);
    }

    @Override
    public void edit(String enterpriseId, String name) {
        enterpriseDao.editEnterpriseById(enterpriseId, name);
    }
}
