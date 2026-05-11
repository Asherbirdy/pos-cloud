package com.app.security.service;

import com.app.security.dto.Store.StoreWithMembersResponse;

import java.util.List;

public interface StoreService {
    List<StoreWithMembersResponse> getAll();

    void create(String name);

    void edit(String storeId, String name, Boolean isActive, Integer runningDevicesLimit);
}
