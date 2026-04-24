package com.app.security.dao;

import com.app.security.dto.MemberStoreAccess.MemberStoreAccessCreateRequest;

public interface MemberStoreAccessDao {

    void createMemberByIds(MemberStoreAccessCreateRequest memberStoreAccessCreateRequest);
    void getAccessMemberByStoreId(String memberId, String storeId);
    void editMemberByStoreId(String memberId, String storeId);
}
