package com.app.security.dao.impl;

import com.app.security.dao.StoreCheckoutDao;
import com.app.security.enums.OrderStatus;
import com.app.security.model.StoreCheckout;
import com.app.security.rowmapper.StoreCheckoutRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StoreCheckoutDaoImpl implements StoreCheckoutDao {

    private static final String COLUMNS = "store_checkout_id, store_id, store_shift_id, member_id, settle_price, order_status, checkout_at, created_at, updated_at";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final StoreCheckoutRowMapper rowMapper;

    public StoreCheckoutDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                StoreCheckoutRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<StoreCheckout> getAllByStoreId(String storeId) {
        String sql = "SELECT " + COLUMNS + " FROM store_checkout WHERE store_id = :storeId ORDER BY checkout_at DESC";
        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);
        return namedParameterJdbcTemplate.query(sql, map, rowMapper);
    }

    @Override
    public List<StoreCheckout> getAllByShiftId(String storeShiftId) {
        String sql = "SELECT " + COLUMNS + " FROM store_checkout WHERE store_shift_id = :storeShiftId ORDER BY checkout_at DESC";
        Map<String, Object> map = new HashMap<>();
        map.put("storeShiftId", storeShiftId);
        return namedParameterJdbcTemplate.query(sql, map, rowMapper);
    }

    @Override
    public StoreCheckout getById(String storeCheckoutId) {
        String sql = "SELECT " + COLUMNS + " FROM store_checkout WHERE store_checkout_id = :storeCheckoutId";
        Map<String, Object> map = new HashMap<>();
        map.put("storeCheckoutId", storeCheckoutId);
        List<StoreCheckout> list = namedParameterJdbcTemplate.query(sql, map, rowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public String create(String storeId, String storeShiftId, String memberId, BigDecimal settlePrice) {
        String storeCheckoutId = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO store_checkout(store_checkout_id, store_id, store_shift_id, member_id, settle_price, order_status, checkout_at, created_at, updated_at)
                VALUES (:storeCheckoutId, :storeId, :storeShiftId, :memberId, :settlePrice, :orderStatus, NOW(), NOW(), NOW())
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeCheckoutId", storeCheckoutId);
        map.put("storeId", storeId);
        map.put("storeShiftId", storeShiftId);
        map.put("memberId", memberId);
        map.put("settlePrice", settlePrice);
        map.put("orderStatus", OrderStatus.COMPLETED.name());
        namedParameterJdbcTemplate.update(sql, map);
        return storeCheckoutId;
    }

    @Override
    public void cancel(String storeCheckoutId) {
        String sql = """
                UPDATE store_checkout
                SET order_status = :orderStatus,
                    updated_at = NOW()
                WHERE store_checkout_id = :storeCheckoutId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeCheckoutId", storeCheckoutId);
        map.put("orderStatus", OrderStatus.CANCELLED.name());
        namedParameterJdbcTemplate.update(sql, map);
    }
}
