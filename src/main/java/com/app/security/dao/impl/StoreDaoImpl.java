package com.app.security.dao.impl;

import com.app.security.dao.StoreDao;
import com.app.security.model.Store;
import com.app.security.rowmapper.StoreRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StoreDaoImpl implements StoreDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final StoreRowMapper rowMapper;

    public StoreDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, StoreRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Store> getAllStoreByEnterpriseId(String enterpriseId) {
        String sql = """
                SELECT store_id, enterprise_id, name, is_active, created_at, updated_at
                FROM store
                WHERE enterprise_id = :enterpriseId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("enterpriseId", enterpriseId);

        return namedParameterJdbcTemplate.query(sql, map, rowMapper);

    }

    @Override
    public void createStore(String enterpriseId, String name) {
        String storeId = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO store(store_id, enterprise_id, name, created_at, updated_at)
                VALUES (:store_id, :enterpriseId, :name, NOW(), NOW())
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("store_id", storeId);
        map.put("enterpriseId", enterpriseId);
        map.put("name", name);
        namedParameterJdbcTemplate.update(sql, map);

    }

    @Override
    public void editStore(String store_id, String name) {
        String sql = """
                UPDATE store
                SET name = :name
                WHERE store_id = :storeId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeId", store_id);
        map.put("name", name);
        namedParameterJdbcTemplate.update(sql, map);

    }
}
