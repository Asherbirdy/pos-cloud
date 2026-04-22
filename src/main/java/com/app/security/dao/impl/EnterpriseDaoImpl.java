package com.app.security.dao.impl;

import com.app.security.dao.EnterpriseDao;
import com.app.security.model.Enterprise;
import com.app.security.rowmapper.EnterpriseRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnterpriseDaoImpl implements EnterpriseDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EnterpriseDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Enterprise> getAllEnterprise() {
        String sql = """
                SELECT enterprise_id, name, created_at, updated_at 
                FROM enterprise
                """;

        Map<String, Object> map = new HashMap<>();

        return namedParameterJdbcTemplate.query(sql, map, new EnterpriseRowMapper());
    }

    ;

    @Override
    public void createEnterprise(String name) {
        String enterpriseId = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO enterprise (enterprise_id, name, created_at, updated_at)
                VALUES (:enterpriseId, :name, NOW(), NOW())
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("enterpriseId", enterpriseId);
        map.put("name", name);
        namedParameterJdbcTemplate.update(sql, map);
    }

    ;

    @Override
    public void editEnterpriseById(String enterprise_id, String name) {
        String sql = """
                UPDATE  enterprise
                SET name = :name,
                    updated_at = NOW()
                WHERE enterprise_id = :enterprise_id
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("enterprise_id", enterprise_id);

        namedParameterJdbcTemplate.update(sql, map);

    }

    ;


}
