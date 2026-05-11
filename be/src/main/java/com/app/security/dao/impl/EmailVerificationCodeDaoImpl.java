package com.app.security.dao.impl;

import com.app.security.dao.EmailVerificationCodeDao;
import com.app.security.model.EmailVerificationCode;
import com.app.security.rowmapper.EmailVerificationCodeRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class EmailVerificationCodeDaoImpl implements EmailVerificationCodeDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final EmailVerificationCodeRowMapper rowMapper;

    public EmailVerificationCodeDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                        EmailVerificationCodeRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public String createCode(EmailVerificationCode code) {
        String id = UUID.randomUUID().toString();

        String sql = """
                INSERT INTO email_verification_code
                    (email_verification_code_id, email, code_hash, expires_at, attempts, consumed, created_at)
                VALUES (:id, :email, :codeHash, :expiresAt, 0, FALSE, NOW())
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("email", code.getEmail());
        map.put("codeHash", code.getCodeHash());
        map.put("expiresAt", code.getExpiresAt());

        namedParameterJdbcTemplate.update(sql, map);
        return id;
    }

    @Override
    public EmailVerificationCode getLatestActiveByEmail(String email) {
        String sql = """
                SELECT email_verification_code_id, email, code_hash, expires_at, attempts, consumed, created_at
                FROM email_verification_code
                WHERE email = :email AND consumed = FALSE
                ORDER BY created_at DESC
                LIMIT 1
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<EmailVerificationCode> list = namedParameterJdbcTemplate.query(sql, map, rowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void incrementAttempts(String emailVerificationCodeId) {
        String sql = """
                UPDATE email_verification_code
                SET attempts = attempts + 1
                WHERE email_verification_code_id = :id
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("id", emailVerificationCodeId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void consume(String emailVerificationCodeId) {
        String sql = """
                UPDATE email_verification_code
                SET consumed = TRUE
                WHERE email_verification_code_id = :id
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("id", emailVerificationCodeId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void invalidateAllByEmail(String email) {
        String sql = """
                UPDATE email_verification_code
                SET consumed = TRUE
                WHERE email = :email AND consumed = FALSE
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        namedParameterJdbcTemplate.update(sql, map);
    }
}
