package com.app.security.dao.impl;

import com.app.security.dao.MemberStoreAccessDao;
import com.app.security.dto.Auth.StoreAccessItem;
import com.app.security.enums.StoreRole;
import com.app.security.model.MemberStoreAccess;
import com.app.security.rowmapper.MemberStoreAccessRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class MemberStoreAccessDaoImpl implements MemberStoreAccessDao {


    private static final Logger log = LoggerFactory.getLogger(MemberStoreAccessDaoImpl.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final MemberStoreAccessRowMapper memberStoreAccessRowMapper;

    public MemberStoreAccessDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MemberStoreAccessRowMapper memberStoreAccessRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.memberStoreAccessRowMapper = memberStoreAccessRowMapper;
    }

    @Override
    public void createMemberByIds(String memberId, String storeId, StoreRole role) {

        String sql = """
                INSERT INTO member_store_access(member_store_access_id, member_id, store_id, role)
                VALUES (:memberStoreAccessId,
                        :memberId,
                        :storeId,
                        :role)
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberStoreAccessId", UUID.randomUUID().toString());
        map.put("memberId", memberId);
        map.put("storeId", storeId);
        map.put("role", role.name());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public List<MemberStoreAccess> getAccessByStoreId(String storeId) {
        String sql = """
                SELECT member_store_access_id, member_id, store_id, role, is_active, created_at
                FROM member_store_access
                WHERE store_id = :storeId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);

        return namedParameterJdbcTemplate.query(sql, map, memberStoreAccessRowMapper);
    }

    @Override
    public MemberStoreAccess getByMemberAndStore(String memberId, String storeId) {
        String sql = """
                SELECT member_store_access_id, member_id, store_id, role, is_active, created_at
                FROM member_store_access
                WHERE member_id = :memberId AND store_id = :storeId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("storeId", storeId);

        List<MemberStoreAccess> list = namedParameterJdbcTemplate.query(sql, map, memberStoreAccessRowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<MemberStoreAccess> getActiveAccessByMemberId(String memberId) {
        String sql = """
                SELECT member_store_access_id, member_id, store_id, role, is_active, created_at
                FROM member_store_access
                WHERE member_id = :memberId AND is_active = TRUE
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        return namedParameterJdbcTemplate.query(sql, map, memberStoreAccessRowMapper);
    }

    @Override
    public List<StoreAccessItem> getStoreAccessItemsByMemberId(String memberId) {
        String sql = """
                SELECT msa.store_id      AS store_id,
                       s.name            AS store_name,
                       s.is_active       AS store_active,
                       msa.role          AS role,
                       msa.is_active     AS access_active
                FROM member_store_access msa
                LEFT JOIN store s ON s.store_id = msa.store_id
                WHERE msa.member_id = :memberId AND msa.is_active = TRUE
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        RowMapper<StoreAccessItem> mapper = (rs, rowNum) -> new StoreAccessItem(
                rs.getString("store_id"),
                rs.getString("store_name"),
                rs.getObject("store_active") == null ? null : rs.getBoolean("store_active"),
                rs.getString("role"),
                rs.getObject("access_active") == null ? null : rs.getBoolean("access_active")
        );

        return namedParameterJdbcTemplate.query(sql, map, mapper);
    }

    @Override
    public List<StoreAccessItem> getAllStoreAccessItemsByMemberId(String memberId) {
        String sql = """
                SELECT msa.store_id      AS store_id,
                       s.name            AS store_name,
                       s.is_active       AS store_active,
                       msa.role          AS role,
                       msa.is_active     AS access_active
                FROM member_store_access msa
                LEFT JOIN store s ON s.store_id = msa.store_id
                WHERE msa.member_id = :memberId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        RowMapper<StoreAccessItem> mapper = (rs, rowNum) -> new StoreAccessItem(
                rs.getString("store_id"),
                rs.getString("store_name"),
                rs.getObject("store_active") == null ? null : rs.getBoolean("store_active"),
                rs.getString("role"),
                rs.getObject("access_active") == null ? null : rs.getBoolean("access_active")
        );

        return namedParameterJdbcTemplate.query(sql, map, mapper);
    }

    @Override
    public void updateById(String memberStoreAccessId, StoreRole role, Boolean isActive) {
        String sql = """
                UPDATE member_store_access
                SET role      = :role,
                    is_active = :isActive
                WHERE member_store_access_id = :memberStoreAccessId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberStoreAccessId", memberStoreAccessId);
        map.put("role", role == null ? null : role.name());
        map.put("isActive", isActive);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
