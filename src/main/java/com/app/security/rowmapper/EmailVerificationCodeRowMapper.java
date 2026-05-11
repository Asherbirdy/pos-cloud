package com.app.security.rowmapper;

import com.app.security.model.EmailVerificationCode;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmailVerificationCodeRowMapper implements RowMapper<EmailVerificationCode> {

    @Override
    public EmailVerificationCode mapRow(ResultSet resultSet, int i) throws SQLException {
        EmailVerificationCode code = new EmailVerificationCode();
        code.setEmailVerificationCodeId(resultSet.getString("email_verification_code_id"));
        code.setEmail(resultSet.getString("email"));
        code.setCodeHash(resultSet.getString("code_hash"));
        code.setExpiresAt(resultSet.getTimestamp("expires_at"));
        code.setAttempts(resultSet.getInt("attempts"));
        code.setConsumed(resultSet.getBoolean("consumed"));
        code.setCreatedAt(resultSet.getTimestamp("created_at"));
        return code;
    }
}
