package com.app.security.dao;

import com.app.security.model.EmailVerificationCode;

public interface EmailVerificationCodeDao {

    String createCode(EmailVerificationCode code);

    EmailVerificationCode getLatestActiveByEmail(String email);

    void incrementAttempts(String emailVerificationCodeId);

    void consume(String emailVerificationCodeId);

    void invalidateAllByEmail(String email);
}
