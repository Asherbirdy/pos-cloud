package com.app.security.service;

import com.app.security.dto.MemberStoreAccess.StoreMemberAccessItem;
import com.app.security.enums.StoreRole;

import java.util.List;

public interface MemberStoreAccessService {

    void create(String memberId, String storeId, StoreRole role);

    List<StoreMemberAccessItem> getByStoreId(String storeId);

    void update(String memberStoreAccessId, StoreRole role, Boolean isActive);
}
