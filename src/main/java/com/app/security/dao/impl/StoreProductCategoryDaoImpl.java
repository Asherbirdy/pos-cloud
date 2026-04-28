package com.app.security.dao.impl;

import com.app.security.dao.StoreProductCategoryDao;
import com.app.security.model.StoreProductCategory;
import com.app.security.rowmapper.StoreProductCategoryRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StoreProductCategoryDaoImpl implements StoreProductCategoryDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final StoreProductCategoryRowMapper rowMapper;

    public StoreProductCategoryDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                       StoreProductCategoryRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<StoreProductCategory> getAllByStoreId(String storeId) {
        String sql = """
                SELECT product_category_id, name, store_id, created_at, updated_at
                FROM store_product_category
                WHERE store_id = :storeId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);

        return namedParameterJdbcTemplate.query(sql, map, rowMapper);
    }

    @Override
    public StoreProductCategory getById(String productCategoryId) {
        String sql = """
                SELECT product_category_id, name, store_id, created_at, updated_at
                FROM store_product_category
                WHERE product_category_id = :productCategoryId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("productCategoryId", productCategoryId);

        List<StoreProductCategory> list = namedParameterJdbcTemplate.query(sql, map, rowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void create(String storeId, String name) {
        String productCategoryId = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO store_product_category(product_category_id, name, store_id, created_at, updated_at)
                VALUES (:productCategoryId, :name, :storeId, NOW(), NOW())
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("productCategoryId", productCategoryId);
        map.put("name", name);
        map.put("storeId", storeId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void update(String productCategoryId, String name) {
        String sql = """
                UPDATE store_product_category
                SET name = :name,
                    updated_at = NOW()
                WHERE product_category_id = :productCategoryId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("productCategoryId", productCategoryId);
        map.put("name", name);
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void delete(String productCategoryId) {
        String sql = """
                DELETE FROM store_product_category
                WHERE product_category_id = :productCategoryId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("productCategoryId", productCategoryId);
        namedParameterJdbcTemplate.update(sql, map);
    }
}
