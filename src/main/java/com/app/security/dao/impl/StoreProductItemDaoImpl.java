package com.app.security.dao.impl;

import com.app.security.dao.StoreProductItemDao;
import com.app.security.model.StoreProductItem;
import com.app.security.rowmapper.StoreProductItemRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StoreProductItemDaoImpl implements StoreProductItemDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final StoreProductItemRowMapper rowMapper;

    public StoreProductItemDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                   StoreProductItemRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<StoreProductItem> getAllByCategoryId(String storeProductCategoryId) {
        String sql = """
                SELECT store_product_item_id, store_product_category_id, name, current_price, created_at, updated_at
                FROM store_product_item
                WHERE store_product_category_id = :storeProductCategoryId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeProductCategoryId", storeProductCategoryId);

        return namedParameterJdbcTemplate.query(sql, map, rowMapper);
    }

    @Override
    public StoreProductItem getById(String storeProductItemId) {
        String sql = """
                SELECT store_product_item_id, store_product_category_id, name, current_price, created_at, updated_at
                FROM store_product_item
                WHERE store_product_item_id = :storeProductItemId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeProductItemId", storeProductItemId);

        List<StoreProductItem> list = namedParameterJdbcTemplate.query(sql, map, rowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void create(String storeProductCategoryId, String name, BigDecimal currentPrice) {
        String storeProductItemId = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO store_product_item(store_product_item_id, store_product_category_id, name, current_price, created_at, updated_at)
                VALUES (:storeProductItemId, :storeProductCategoryId, :name, :currentPrice, NOW(), NOW())
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeProductItemId", storeProductItemId);
        map.put("storeProductCategoryId", storeProductCategoryId);
        map.put("name", name);
        map.put("currentPrice", currentPrice);
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void update(String storeProductItemId, String name, BigDecimal currentPrice) {
        String sql = """
                UPDATE store_product_item
                SET name = :name,
                    current_price = :currentPrice,
                    updated_at = NOW()
                WHERE store_product_item_id = :storeProductItemId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeProductItemId", storeProductItemId);
        map.put("name", name);
        map.put("currentPrice", currentPrice);
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void delete(String storeProductItemId) {
        String sql = """
                DELETE FROM store_product_item
                WHERE store_product_item_id = :storeProductItemId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeProductItemId", storeProductItemId);
        namedParameterJdbcTemplate.update(sql, map);
    }
}
