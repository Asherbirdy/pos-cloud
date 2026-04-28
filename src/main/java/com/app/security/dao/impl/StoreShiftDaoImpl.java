package com.app.security.dao.impl;

import com.app.security.dao.StoreShiftDao;
import com.app.security.enums.ShiftStatus;
import com.app.security.model.StoreShift;
import com.app.security.rowmapper.StoreShiftRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StoreShiftDaoImpl implements StoreShiftDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final StoreShiftRowMapper rowMapper;

    public StoreShiftDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                             StoreShiftRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<StoreShift> getAllByStoreId(String storeId) {
        String sql = """
                SELECT store_shift_id, store_id, member_id, date, status, open_time, close_time, created_at, updated_at
                FROM store_shift
                WHERE store_id = :storeId
                ORDER BY CASE WHEN status = 'OPEN' THEN 0 ELSE 1 END, open_time DESC
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);

        return namedParameterJdbcTemplate.query(sql, map, rowMapper);
    }

    @Override
    public StoreShift getById(String storeShiftId) {
        String sql = """
                SELECT store_shift_id, store_id, member_id, date, status, open_time, close_time, created_at, updated_at
                FROM store_shift
                WHERE store_shift_id = :storeShiftId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeShiftId", storeShiftId);

        List<StoreShift> list = namedParameterJdbcTemplate.query(sql, map, rowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int countOpenByStoreId(String storeId) {
        String sql = """
                SELECT COUNT(*)
                FROM store_shift
                WHERE store_id = :storeId AND status = :status
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("status", ShiftStatus.OPEN.name());

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public String openShift(String storeId, String memberId) {
        String storeShiftId = UUID.randomUUID().toString();
        String sql = """
                INSERT INTO store_shift(store_shift_id, store_id, member_id, date, status, open_time, created_at, updated_at)
                VALUES (:storeShiftId, :storeId, :memberId, CURRENT_DATE, :status, NOW(), NOW(), NOW())
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeShiftId", storeShiftId);
        map.put("storeId", storeId);
        map.put("memberId", memberId);
        map.put("status", ShiftStatus.OPEN.name());
        namedParameterJdbcTemplate.update(sql, map);
        return storeShiftId;
    }

    @Override
    public void closeShift(String storeShiftId) {
        String sql = """
                UPDATE store_shift
                SET status = :status,
                    close_time = NOW(),
                    updated_at = NOW()
                WHERE store_shift_id = :storeShiftId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("storeShiftId", storeShiftId);
        map.put("status", ShiftStatus.CLOSED.name());
        namedParameterJdbcTemplate.update(sql, map);
    }
}
