package com.app.security.service.impl;

import com.app.security.dao.StoreDao;
import com.app.security.model.Store;
import com.app.security.service.StoreService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreServiceImpl implements StoreService {

    private final StoreDao storeDao;

    public StoreServiceImpl(StoreDao storeDao) {
        this.storeDao = storeDao;
    }

    @Override
    public List<Store> getAllByEnterpriseId(String enterpriseId) {
        return storeDao.getAllStoreByEnterpriseId(enterpriseId);
    }

    @Override
    public void create(String enterpriseId, String name) {
        storeDao.createStore(enterpriseId, name);
    }

    @Override
    public void edit(String storeId, String name) {
        storeDao.editStore(storeId, name);
    }
}
