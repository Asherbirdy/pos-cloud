package com.app.security.service.impl;

import com.app.security.model.MemberStoreAccess;
import com.app.security.service.MemberStoreAccessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberStoreAccessServiceImpl implements MemberStoreAccessService {

    @Override
    public void create(String memberId, String storeId, String role) {
        // TODO: implement with DAO
    }

    @Override
    public List<MemberStoreAccess> getByStoreId(String storeId) {
        // TODO: implement with DAO
        return List.of();
    }

    @Override
    public void update(String memberStoreAccessId, String role, String status) {
        // TODO: implement with DAO
    }
}
