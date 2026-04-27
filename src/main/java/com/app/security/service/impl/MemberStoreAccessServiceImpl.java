package com.app.security.service.impl;

import com.app.security.dao.MemberStoreAccessDao;
import com.app.security.enums.StoreRole;
import com.app.security.model.MemberStoreAccess;
import com.app.security.service.MemberStoreAccessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberStoreAccessServiceImpl implements MemberStoreAccessService {

    private final MemberStoreAccessDao memberStoreAccessDao;

    public MemberStoreAccessServiceImpl(MemberStoreAccessDao memberStoreAccessDao) {
        this.memberStoreAccessDao = memberStoreAccessDao;
    }

    @Override
    public void create(String memberId, String storeId, StoreRole role) {
        memberStoreAccessDao.createMemberByIds(memberId, storeId, role);
    }

    @Override
    public List<MemberStoreAccess> getByStoreId(String storeId) {
        return memberStoreAccessDao.getAccessByStoreId(storeId);
    }

    @Override
    public void update(String memberStoreAccessId, StoreRole role, Boolean isActive) {
        memberStoreAccessDao.updateById(memberStoreAccessId, role, isActive);
    }
}
