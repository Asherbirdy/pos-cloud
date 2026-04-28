package com.app.security.dao.impl;

import com.app.security.dao.ProductCategoryDao;
import com.app.security.model.ProductCategory;
import com.app.security.rowmapper.ProductCategoryRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ProductCategoryDaoImpl implements ProductCategoryDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final ProductCategoryRowMapper rowMapper;

    public ProductCategoryDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                  ProductCategoryRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<ProductCategory> getAllByStoreId(String storeId) {
        String sql = """
                SELECT product_category_id, name, store_id, created_at, updated_at
                FROM product_category
                WHERE store_id = :storeId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);

        return namedParameterJdbcTemplate.query(sql, map, rowMapper);
    }

    @Override
    public ProductCategory getById(String productCategoryId) {
        String sql = """
                SELECT product_category_id, name, store_id, created_at, updated_at
                FROM product_category
                WHERE product_category_id = :productCategoryId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("productCategoryId", productCategoryId);

        List<ProductCategory> list = namedParameterJdbcTemplate.query(sql, map, rowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void create(String storeId, String name) {
        String productCategoryId = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO product_category(product_category_id, name, store_id, created_at, updated_at)
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
                UPDATE product_category
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
                DELETE FROM product_category
                WHERE product_category_id = :productCategoryId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("productCategoryId", productCategoryId);
        namedParameterJdbcTemplate.update(sql, map);
    }
}
