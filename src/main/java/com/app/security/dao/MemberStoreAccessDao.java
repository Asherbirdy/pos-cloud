package com.app.security.dao;

import com.app.security.dto.MemberStoreAccess.MemberStoreAccessCreateRequest;

public interface MemberStoreAccessDao {

    void createMemberByIds(String memberId, String storeId);
    void getAccessByStoreId(MemberStoreAccessCreateRequest memberStoreAccessCreateRequest);
    void editMemberByStoreId(String memberId, String storeId);
}
