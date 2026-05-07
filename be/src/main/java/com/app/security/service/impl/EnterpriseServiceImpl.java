package com.app.security.service.impl;

import com.app.security.dao.EnterpriseDao;
import com.app.security.dao.StoreDao;
import com.app.security.dto.Enterprise.EnterpriseWithStoresResponse;
import com.app.security.model.Enterprise;
import com.app.security.model.Store;
import com.app.security.service.EnterpriseService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseDao enterpriseDao;
    private final StoreDao storeDao;

    public EnterpriseServiceImpl(EnterpriseDao enterpriseDao, StoreDao storeDao) {
        this.enterpriseDao = enterpriseDao;
        this.storeDao = storeDao;
    }

    @Override
    public List<EnterpriseWithStoresResponse> getAll() {
        List<Enterprise> enterprises = enterpriseDao.getAllEnterprise();
        List<EnterpriseWithStoresResponse> result = new ArrayList<>();
        for (Enterprise enterprise : enterprises) {
            List<Store> stores = storeDao.getAllStoreByEnterpriseId(enterprise.getEnterprise_id());

            EnterpriseWithStoresResponse dto = new EnterpriseWithStoresResponse();
            dto.setEnterprise_id(enterprise.getEnterprise_id());
            dto.setName(enterprise.getName());
            dto.setCreatedAt(enterprise.getCreatedAt());
            dto.setUpdatedAt(enterprise.getUpdatedAt());
            dto.setStores(stores);
            result.add(dto);
        }
        return result;
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
