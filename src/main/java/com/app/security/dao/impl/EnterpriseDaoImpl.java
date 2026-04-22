package com.app.security.dao.impl;

import com.app.security.dao.EnterpriseDao;
import com.app.security.model.Enterprise;
import com.app.security.rowmapper.EnterpriseRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnterpriseDaoImpl implements EnterpriseDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EnterpriseDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Enterprise> getAllEnterprise() {
        String sql = "SELECT enterprise_id, name, created_at, updated_at FROM enterprise";

        Map<String, Object> map = new HashMap<>();

        return namedParameterJdbcTemplate.query(sql, map, new EnterpriseRowMapper());
    };

    @Override
    public void createEnterprise(Enterprise enterprise) {
    };

    @Override
    public void editEnterpriseById(String id, String name) {
    };
}
