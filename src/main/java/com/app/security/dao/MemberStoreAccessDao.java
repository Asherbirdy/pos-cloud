package com.app.security.dao;

import com.app.security.dto.MemberStoreAccess.MemberStoreAccessCreateRequest;

public interface MemberStoreAccessDao {

    void getAccessByStoreId(MemberStoreAccessCreateRequest memberStoreAccessCreateRequest);
    void createMemberByIds(String memberId, String storeId);
    void editMemberByStoreId(String memberId, String storeId);
}
