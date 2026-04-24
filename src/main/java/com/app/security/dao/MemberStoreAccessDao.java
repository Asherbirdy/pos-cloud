package com.app.security.dao;

public interface MemberStoreAccessDao {

    void getAccessByStoreId(String memberId, String storeId);
    void createMemberByIds(String memberId, String storeId);
    void editMemberByStoreId(String memberId, String storeId);
}
