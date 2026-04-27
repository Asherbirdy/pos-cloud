package com.app.security.dao;

import com.app.security.enums.StoreRole;
import com.app.security.model.MemberStoreAccess;

import java.util.List;

public interface MemberStoreAccessDao {

    void createMemberByIds(String memberId, String storeId, StoreRole role);
    List<MemberStoreAccess> getAccessByStoreId(String storeId);
    void editMemberByStoreId(String memberId, String storeId);
}
