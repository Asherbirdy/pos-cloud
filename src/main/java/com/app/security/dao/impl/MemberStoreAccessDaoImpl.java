package com.app.security.dao.impl;

import com.app.security.dao.MemberStoreAccessDao;
import com.app.security.dto.MemberStoreAccess.MemberStoreAccessCreateRequest;
import com.app.security.model.MemberStoreAccess;
import com.app.security.rowmapper.MemberStoreAccessRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberStoreAccessDaoImpl implements MemberStoreAccessDao {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final MemberStoreAccessRowMapper memberStoreAccessRowMapper;

    public MemberStoreAccessDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MemberStoreAccessRowMapper memberStoreAccessRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.memberStoreAccessRowMapper = memberStoreAccessRowMapper;
    }

    @Override
    public void createMemberByIds(MemberStoreAccessCreateRequest memberStoreAccessCreateRequest) {

    }

    @Override
    public void getAccessMemberByStoreId(String memberId, String storeId) {

    }

    @Override
    public void editMemberByStoreId(String memberId, String storeId) {

    }
}
