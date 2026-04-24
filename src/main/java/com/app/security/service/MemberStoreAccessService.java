package com.app.security.service;

import com.app.security.model.MemberStoreAccess;

import java.util.List;

public interface MemberStoreAccessService {

    void create(String memberId, String storeId, String role);

    List<MemberStoreAccess> getByStoreId(String storeId);

    void update(String memberStoreAccessId, String role, String status);
}
