package com.app.security.rowmapper;

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
        access.setId(resultSet.getString("id"));
        access.setMemberId(resultSet.getString("member_id"));
        access.setEnterpriseId(resultSet.getString("enterprise_id"));
        access.setStoreId(resultSet.getString("store_id"));
        access.setRole(resultSet.getString("role"));
        access.setStatus(resultSet.getString("status"));
        access.setCreatedAt(resultSet.getTimestamp("created_at"));
        return access;
    }
}
