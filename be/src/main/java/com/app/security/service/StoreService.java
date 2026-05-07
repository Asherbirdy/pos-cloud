package com.app.security.service;

import com.app.security.model.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAll();

    void create(String name);

    void edit(String storeId, String name, Boolean isActive, Integer runningDevicesLimit);
}
