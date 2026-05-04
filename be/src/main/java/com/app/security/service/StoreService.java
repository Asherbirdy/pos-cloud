package com.app.security.service;

import com.app.security.model.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAllByEnterpriseId(String enterpriseId);

    void create(String enterpriseId, String name);

    void edit(String storeId, String name, Boolean isActive, Integer runningDevicesLimit);
}
