package com.app.security.rowmapper;

import com.app.security.model.Enterprise;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EnterpriseRowMapper implements RowMapper<Enterprise> {

    @Override
    public Enterprise mapRow(ResultSet resultSet, int i) throws SQLException {
        Enterprise enterprise = new Enterprise();
        enterprise.setEnterprise_id(resultSet.getString("enterprise_id"));
        enterprise.setName(resultSet.getString("name"));
        enterprise.setCreatedAt(resultSet.getTimestamp("created_at"));
        enterprise.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return enterprise;
    }
}
