package com.app.security.dao;

import com.app.security.model.Store;

import java.util.List;

public interface StoreDao {

    List<Store> getAllStores();

    Store getStoreById(String storeId);

    void createStore(String name);

    void editStore(String store_id, String name, Boolean isActive, Integer runningDevicesLimit);

}
