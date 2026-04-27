package com.app.security.dao.impl;

import com.app.security.dao.MemberStoreAccessDao;
import com.app.security.enums.StoreRole;
import com.app.security.model.MemberStoreAccess;
import com.app.security.rowmapper.MemberStoreAccessRowMapper;
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
                INSERT INTO member_store_access(member_store_access_id, member_id, enterprise_id, store_id, role)
                VALUES (:memberStoreAccessId,
                        :memberId,
                        (SELECT enterprise_id FROM store WHERE store_id = :storeId),
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
                SELECT member_store_access_id, member_id, enterprise_id, store_id, role, status, created_at
                FROM member_store_access
                WHERE store_id = :storeId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);

        return namedParameterJdbcTemplate.query(sql, map, memberStoreAccessRowMapper);
    }

    @Override
    public void updateById(String memberStoreAccessId, StoreRole role, String status) {
        String sql = """
                UPDATE member_store_access
                SET role   = :role,
                    status = :status
                WHERE member_store_access_id = :memberStoreAccessId
                """;

        System.out.println(sql);
        Map<String, Object> map = new HashMap<>();
        map.put("memberStoreAccessId", memberStoreAccessId);
        map.put("role", role == null ? null : role.name());
        map.put("status", status);
        System.out.println("noa" + map);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
