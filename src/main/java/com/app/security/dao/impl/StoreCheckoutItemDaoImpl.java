package com.app.security.dao.impl;

import com.app.security.dao.StoreCheckoutItemDao;
import com.app.security.model.StoreCheckoutItem;
import com.app.security.rowmapper.StoreCheckoutItemRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StoreCheckoutItemDaoImpl implements StoreCheckoutItemDao {

    private static final String COLUMNS = "store_checkout_item_id, store_checkout_id, store_product_item_id, quantity, unit_price, subtotal, created_at";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final StoreCheckoutItemRowMapper rowMapper;

    public StoreCheckoutItemDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                    StoreCheckoutItemRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<StoreCheckoutItem> getAllByCheckoutId(String storeCheckoutId) {
        String sql = "SELECT " + COLUMNS + " FROM store_checkout_item WHERE store_checkout_id = :storeCheckoutId ORDER BY created_at ASC";
        Map<String, Object> map = new HashMap<>();
        map.put("storeCheckoutId", storeCheckoutId);
        return namedParameterJdbcTemplate.query(sql, map, rowMapper);
    }

    @Override
    public StoreCheckoutItem getById(String storeCheckoutItemId) {
        String sql = "SELECT " + COLUMNS + " FROM store_checkout_item WHERE store_checkout_item_id = :storeCheckoutItemId";
        Map<String, Object> map = new HashMap<>();
        map.put("storeCheckoutItemId", storeCheckoutItemId);
        List<StoreCheckoutItem> list = namedParameterJdbcTemplate.query(sql, map, rowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public String create(String storeCheckoutId, String storeProductItemId, Integer quantity, BigDecimal unitPrice, BigDecimal subtotal) {
        String storeCheckoutItemId = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO store_checkout_item(store_checkout_item_id, store_checkout_id, store_product_item_id, quantity, unit_price, subtotal, created_at)
                VALUES (:storeCheckoutItemId, :storeCheckoutId, :storeProductItemId, :quantity, :unitPrice, :subtotal, NOW())
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeCheckoutItemId", storeCheckoutItemId);
        map.put("storeCheckoutId", storeCheckoutId);
        map.put("storeProductItemId", storeProductItemId);
        map.put("quantity", quantity);
        map.put("unitPrice", unitPrice);
        map.put("subtotal", subtotal);
        namedParameterJdbcTemplate.update(sql, map);
        return storeCheckoutItemId;
    }

    @Override
    public void delete(String storeCheckoutItemId) {
        String sql = "DELETE FROM store_checkout_item WHERE store_checkout_item_id = :storeCheckoutItemId";
        Map<String, Object> map = new HashMap<>();
        map.put("storeCheckoutItemId", storeCheckoutItemId);
        namedParameterJdbcTemplate.update(sql, map);
    }
}
