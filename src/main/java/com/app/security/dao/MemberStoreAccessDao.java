package com.app.security.dao;

import com.app.security.enums.StoreRole;

public interface MemberStoreAccessDao {

    void createMemberByIds(String memberId, String storeId, StoreRole role);
    void getAccessMemberByStoreId(String memberId, String storeId);
    void editMemberByStoreId(String memberId, String storeId);
}
