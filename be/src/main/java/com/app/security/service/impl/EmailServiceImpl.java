package com.app.security.service.impl;

import com.app.security.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from:}")
    private String mailFrom;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (mailFrom != null && !mailFrom.isBlank()) {
            message.setFrom(mailFrom);
        }
        message.setTo(toEmail);
        message.setSubject("Your POS Cloud login verification code");
        message.setText("Your verification code is: " + code + "\n\n"
                + "This code will expire in 5 minutes. If you did not request this, please ignore this email.");
        mailSender.send(message);
    }
}
