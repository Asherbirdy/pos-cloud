package com.app.security.service;

public interface EmailService {

    void sendVerificationCode(String toEmail, String code);
}
