package com.app.security.service.impl;

import com.app.security.dao.MemberStoreAccessDao;
import com.app.security.dao.StoreDao;
import com.app.security.dto.MemberStoreAccess.StoreMemberAccessItem;
import com.app.security.dto.Store.StoreWithMembersResponse;
import com.app.security.model.Store;
import com.app.security.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class StoreServiceImpl implements StoreService {

    private final StoreDao storeDao;
    private final MemberStoreAccessDao memberStoreAccessDao;

    public StoreServiceImpl(StoreDao storeDao, MemberStoreAccessDao memberStoreAccessDao) {
        this.storeDao = storeDao;
        this.memberStoreAccessDao = memberStoreAccessDao;
    }

    @Override
    public List<StoreWithMembersResponse> getAll() {
        List<Store> stores = storeDao.getAllStores();
        List<StoreWithMembersResponse> result = new ArrayList<>();
        for (Store store : stores) {
            List<StoreMemberAccessItem> members = memberStoreAccessDao
                    .getStoreMembersByStoreId(store.getStore_id());
            result.add(new StoreWithMembersResponse(
                    store.getStore_id(),
                    store.getName(),
                    store.getActive(),
                    store.getRunning_devices_limit(),
                    store.getCreatedAt(),
                    store.getUpdatedAt(),
                    members
            ));
        }
        return result;
    }

    @Override
    public void create(String name) {
        storeDao.createStore(name);
    }

    @Override
    public void edit(String storeId, String name, Boolean isActive, Integer runningDevicesLimit) {

        if (runningDevicesLimit > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RUNNING_DEVICES_LIMIT_ERROR");
        }

        storeDao.editStore(storeId, name, isActive, runningDevicesLimit);
    }
}
