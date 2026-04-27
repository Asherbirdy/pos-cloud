package com.app.security.rowmapper;

import com.app.security.enums.StoreRole;
import com.app.security.model.MemberStoreAccess;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MemberStoreAccessRowMapper implements RowMapper<MemberStoreAccess> {

    @Override
    public MemberStoreAccess mapRow(ResultSet resultSet, int i) throws SQLException {
        MemberStoreAccess access = new MemberStoreAccess();
        access.setMemberStoreAccessId(resultSet.getString("member_store_access_id"));
        access.setMemberId(resultSet.getString("member_id"));
        access.setEnterpriseId(resultSet.getString("enterprise_id"));
        access.setStoreId(resultSet.getString("store_id"));
        access.setRole(StoreRole.valueOf(resultSet.getString("role")));
        access.setIsActive(resultSet.getBoolean("is_active"));
        access.setCreatedAt(resultSet.getTimestamp("created_at"));
        return access;
    }
}
