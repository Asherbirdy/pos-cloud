package com.app.security.service.impl;

import com.app.security.dao.EmailVerificationCodeDao;
import com.app.security.dao.MemberDao;
import com.app.security.dao.MemberStoreAccessDao;
import com.app.security.dao.TokenDao;
import com.app.security.dto.Auth.*;
import com.app.security.enums.StoreRole;
import com.app.security.model.EmailVerificationCode;
import com.app.security.model.MemberStoreAccess;
import com.app.security.model.Member;
import com.app.security.model.Token;
import com.app.security.security.JwtUtil;
import com.app.security.service.AuthService;
import com.app.security.service.EmailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AuthServiceImpl implements AuthService {

    private final MemberDao memberDao;

    private final TokenDao tokenDao;

    private final MemberStoreAccessDao memberStoreAccessDao;

    private final EmailVerificationCodeDao emailVerificationCodeDao;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${auth.email-code.ttl-seconds:300}")
    private long emailCodeTtlSeconds;

    @Value("${auth.email-code.resend-cooldown-seconds:60}")
    private long emailCodeResendCooldownSeconds;

    @Value("${auth.email-code.max-attempts:5}")
    private int emailCodeMaxAttempts;

    public AuthServiceImpl(MemberDao memberDao, TokenDao tokenDao, MemberStoreAccessDao memberStoreAccessDao,
                           EmailVerificationCodeDao emailVerificationCodeDao, EmailService emailService,
                           PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.memberDao = memberDao;
        this.tokenDao = tokenDao;
        this.memberStoreAccessDao = memberStoreAccessDao;
        this.emailVerificationCodeDao = emailVerificationCodeDao;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthRegisterResponse register(RegisterRequest registerRequest) {
        String name = registerRequest.getName();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        // 檢查 email 是否已被註冊
        Member existingMember = memberDao.getMemberByEmail(email);
        if (existingMember != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_USED");
        }

        // hash 密碼並建立會員
        String hashedPassword = passwordEncoder.encode(password);
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setPassword(hashedPassword);
        member.setRole("user");

        String memberId = memberDao.createMember(member);

        // 建立 refresh token 並存入資料庫
        String refreshTokenStr = createRefreshToken(memberId);

        // 產生 JWT 並設定 Cookie
        attachCookieToResponse(memberId, name, email, "user", refreshTokenStr);

        return new AuthRegisterResponse(name, memberId, "user");
    }

    @Override
    public AuthLoginStep1Response loginStep1(LoginStep1Request request) {
        Member member = authenticate(request.getEmail(), request.getPassword());

        if (requiresOtp(member)) {
            issueAndSendOtp(member.getEmail());
            return new AuthLoginStep1Response(true, member.getEmail(), null);
        }

        AuthLoginResponse loginResponse = issueTokens(member);
        return new AuthLoginStep1Response(false, member.getEmail(), loginResponse);
    }

    @Override
    public AuthLoginResponse login(LoginRequest loginRequest) {
        Member member = authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        // 一律驗 OTP（/auth/login 是「帳密 + OTP」入口；不需 OTP 的帳號走 /auth/login/step1）
        verifyOtp(member.getEmail(), loginRequest.getCode());

        return issueTokens(member);
    }

    private Member authenticate(String email, String password) {
        Member member = memberDao.getMemberByEmail(email);
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "WRONG_EMAIL_OR_PASSWORD");
        }
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "WRONG_EMAIL_OR_PASSWORD");
        }
        return member;
    }

    private boolean requiresOtp(Member member) {
        if ("admin".equals(member.getRole())) {
            return true;
        }
        List<MemberStoreAccess> accesses = memberStoreAccessDao.getActiveAccessByMemberId(member.getMemberId());
        for (MemberStoreAccess access : accesses) {
            if (access.getRole() == StoreRole.STORE_MANAGER) {
                return true;
            }
        }
        return false;
    }

    private AuthLoginResponse issueTokens(Member member) {
        String memberId = member.getMemberId();
        String refreshTokenStr;
        Token existingToken = tokenDao.getValidTokenByMemberId(memberId);
        if (existingToken != null) {
            refreshTokenStr = existingToken.getRefreshToken();
        } else {
            refreshTokenStr = createRefreshToken(memberId);
        }

        TokenPair token = attachCookieToResponse(memberId, member.getName(), member.getEmail(), member.getRole(), refreshTokenStr);
        List<StoreAccessItem> storeAccessItems = memberStoreAccessDao.getStoreAccessItemsByMemberId(memberId);
        return new AuthLoginResponse(member.getName(), memberId, member.getRole(), storeAccessItems, token);
    }

    private void issueAndSendOtp(String email) {
        // 60 秒節流：如果最近一筆 active OTP 是 cooldown 內建立的，擋掉
        EmailVerificationCode latest = emailVerificationCodeDao.getLatestActiveByEmail(email);
        if (latest != null && latest.getCreatedAt() != null) {
            long ageMs = System.currentTimeMillis() - latest.getCreatedAt().getTime();
            if (ageMs < emailCodeResendCooldownSeconds * 1000L) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "OTP_RESEND_COOLDOWN");
            }
        }

        // 作廢同 email 所有未用的舊 OTP
        emailVerificationCodeDao.invalidateAllByEmail(email);

        String code = generateSixDigitCode();
        EmailVerificationCode record = new EmailVerificationCode();
        record.setEmail(email);
        record.setCodeHash(passwordEncoder.encode(code));
        record.setExpiresAt(new Date(System.currentTimeMillis() + emailCodeTtlSeconds * 1000L));
        emailVerificationCodeDao.createCode(record);

        emailService.sendVerificationCode(email, code);
    }

    private void verifyOtp(String email, String code) {
        EmailVerificationCode record = emailVerificationCodeDao.getLatestActiveByEmail(email);
        if (record == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP_NOT_FOUND");
        }
        if (record.getExpiresAt().getTime() < System.currentTimeMillis()) {
            emailVerificationCodeDao.consume(record.getEmailVerificationCodeId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP_EXPIRED");
        }
        if (record.getAttempts() != null && record.getAttempts() >= emailCodeMaxAttempts) {
            emailVerificationCodeDao.consume(record.getEmailVerificationCodeId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP_ATTEMPTS_EXCEEDED");
        }
        if (!passwordEncoder.matches(code, record.getCodeHash())) {
            emailVerificationCodeDao.incrementAttempts(record.getEmailVerificationCodeId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP_INVALID");
        }
        emailVerificationCodeDao.consume(record.getEmailVerificationCodeId());
    }

    private String generateSixDigitCode() {
        int n = secureRandom.nextInt(1_000_000);
        return String.format("%06d", n);
    }

    @Override
    public void logout() {
        String memberId = getCurrentMemberId();
        HttpServletResponse response = getCurrentResponse();

        // 刪除該使用者所有 token
        tokenDao.deleteTokensByMemberId(memberId);

        // 清除 Cookie
        Cookie accessCookie = new Cookie("accessToken", "");
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refreshToken", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
    }

    @Override
    public AuthRefreshTokenResponse refreshToken(String authorizationHeader) {
        // 從 Authorization header 取出 refreshToken（Bearer scheme）
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_INVALID");
        }
        String refreshTokenJwt = authorizationHeader.substring("Bearer ".length()).trim();
        if (refreshTokenJwt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_INVALID");
        }

        // 解析 refreshToken JWT
        Claims claims;
        try {
            claims = jwtUtil.parseToken(refreshTokenJwt);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_INVALID");
        }

        String memberId = claims.get("memberId", String.class);
        String refreshTokenStr = claims.get("refreshToken", String.class);
        if (memberId == null || refreshTokenStr == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_INVALID");
        }

        // 確認 token 仍存在 DB 且為 valid
        Token existingToken = tokenDao.getValidTokenByMemberId(memberId);
        if (existingToken == null || !refreshTokenStr.equals(existingToken.getRefreshToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_INVALID");
        }

        // IP 一致性檢查
        HttpServletRequest request = getCurrentRequest();
        String currentIp = request.getRemoteAddr();
        if (existingToken.getIp() != null && !existingToken.getIp().equals(currentIp)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "IP_MISMATCH");
        }

        // 取最新會員資料 + storeAccess，重新產生 accessToken
        Member member = memberDao.getMemberById(memberId);
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_INVALID");
        }
        Map<String, String> storeAccess = buildStoreAccessMap(memberId);
        String newAccessToken = jwtUtil.createAccessToken(
                memberId, member.getName(), member.getEmail(), member.getRole(), storeAccess);

        return new AuthRefreshTokenResponse("TOKEN_REFRESHED", newAccessToken);
    }

    @Override
    public AuthRegisterResponse registerAdmin(RegisterRequest registerRequest) {
        String name = registerRequest.getName();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        // 檢查是否已有 admin
        List<Member> existingAdmins = memberDao.getRoleMembers("admin");
        if (!existingAdmins.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ADMIN_ALREADY_EXISTS");
        }

        // hash 密碼並建立 admin
        String hashedPassword = passwordEncoder.encode(password);
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        member.setPassword(hashedPassword);
        member.setRole("admin");

        String memberId = memberDao.createMember(member);

        // 建立 refresh token 並存入資料庫
        String refreshTokenStr = createRefreshToken(memberId);

        // 產生 JWT 並設定 Cookie
        attachCookieToResponse(memberId, name, email, "admin", refreshTokenStr);

        return new AuthRegisterResponse(name, memberId, "admin");
    }

    private ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    private HttpServletRequest getCurrentRequest() {
        return getRequestAttributes().getRequest();
    }

    private HttpServletResponse getCurrentResponse() {
        return getRequestAttributes().getResponse();
    }

    private String getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getCredentials();
    }

    private String createRefreshToken(String memberId) {
        HttpServletRequest request = getCurrentRequest();
        String refreshTokenStr = UUID.randomUUID().toString();
        Token token = new Token();
        token.setRefreshToken(refreshTokenStr);
        token.setIp(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        token.setIsValid(true);
        token.setMemberId(memberId);
        tokenDao.createToken(token);
        return refreshTokenStr;
    }

    private TokenPair attachCookieToResponse(String memberId, String name, String email, String role, String refreshTokenStr) {
        HttpServletResponse response = getCurrentResponse();
        Map<String, String> storeAccess = buildStoreAccessMap(memberId);
        String accessTokenJwt = jwtUtil.createAccessToken(memberId, name, email, role, storeAccess);
        String refreshTokenJwt = jwtUtil.createRefreshToken(memberId, email, refreshTokenStr);

        Cookie accessCookie = new Cookie("accessToken", accessTokenJwt);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) (jwtUtil.getAccessTokenExpirationMs() / 1000));

        Cookie refreshCookie = new Cookie("refreshToken", refreshTokenJwt);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) (jwtUtil.getRefreshTokenExpirationMs() / 1000));

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return new TokenPair(accessTokenJwt, refreshTokenJwt);
    }

    private Map<String, String> buildStoreAccessMap(String memberId) {
        List<MemberStoreAccess> accesses = memberStoreAccessDao.getActiveAccessByMemberId(memberId);
        Map<String, String> map = new HashMap<>();
        for (MemberStoreAccess access : accesses) {
            map.put(access.getStoreId(), access.getRole().name());
        }
        return map;
    }

}
