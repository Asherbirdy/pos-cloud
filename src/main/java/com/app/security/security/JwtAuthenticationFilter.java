package com.app.security.security;

import com.app.security.dao.MemberDao;
import com.app.security.dao.MemberStoreAccessDao;
import com.app.security.dao.TokenDao;
import com.app.security.model.Member;
import com.app.security.model.MemberStoreAccess;
import com.app.security.model.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenDao tokenDao;
    private final MemberDao memberDao;
    private final MemberStoreAccessDao memberStoreAccessDao;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenDao tokenDao, MemberDao memberDao,
                                   MemberStoreAccessDao memberStoreAccessDao) {
        this.jwtUtil = jwtUtil;
        this.tokenDao = tokenDao;
        this.memberDao = memberDao;
        this.memberStoreAccessDao = memberStoreAccessDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessTokenJwt = getCookieValue(request, "accessToken");
        String refreshTokenJwt = getCookieValue(request, "refreshToken");

        // 嘗試用 accessToken 認證
        if (accessTokenJwt != null) {
            try {
                Claims claims = jwtUtil.parseToken(accessTokenJwt);
                setAuthentication(claims);
                filterChain.doFilter(request, response);
                return;
            } catch (ExpiredJwtException e) {
                // accessToken 過期，嘗試用 refreshToken 刷新
            } catch (Exception e) {
                // accessToken 無效
            }
        }

        // 嘗試用 refreshToken 刷新
        if (refreshTokenJwt != null) {
            try {
                Claims refreshClaims = jwtUtil.parseToken(refreshTokenJwt);
                String memberId = refreshClaims.get("memberId", String.class);
                String refreshTokenStr = refreshClaims.get("refreshToken", String.class);

                // 驗證 refreshToken 是否存在於資料庫且有效
                Token existingToken = tokenDao.getValidTokenByMemberId(memberId);
                if (existingToken != null && existingToken.getRefreshToken().equals(refreshTokenStr)) {
                    // 從資料庫取得最新的會員資訊
                    Member member = memberDao.getMemberById(memberId);
                    if (member != null) {
                        // refresh 時重新撈最新 storeAccess，撤權延遲 = access token TTL
                        Map<String, String> storeAccess = buildStoreAccessMap(memberId);
                        // 重新產生 accessToken 並設定到 Cookie
                        String newAccessToken = jwtUtil.createAccessToken(
                                memberId, member.getName(), member.getEmail(), member.getRole(), storeAccess);

                        Cookie accessCookie = new Cookie("accessToken", newAccessToken);
                        accessCookie.setHttpOnly(true);
                        accessCookie.setPath("/");
                        accessCookie.setMaxAge((int) (jwtUtil.getAccessTokenExpirationMs() / 1000));
                        response.addCookie(accessCookie);

                        // 設定認證
                        Claims newClaims = jwtUtil.parseToken(newAccessToken);
                        setAuthentication(newClaims);
                    }
                }
            } catch (Exception e) {
                // refreshToken 無效或過期
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Claims claims) {
        String email = claims.getSubject();
        String memberId = claims.get("memberId", String.class);
        String role = claims.get("role", String.class);

        // 使用 hasAuthority 對應的權限格式
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        // 將 memberId 存在 credentials 中，方便後續使用
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, memberId, authorities);

        // 將 storeAccess (storeId -> roleName) 放入 details，供 StoreAccessAspect 讀取
        @SuppressWarnings("unchecked")
        Map<String, String> storeAccess = claims.get("storeAccess", Map.class);
        authentication.setDetails(storeAccess == null ? Collections.<String, String>emptyMap() : storeAccess);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Map<String, String> buildStoreAccessMap(String memberId) {
        List<MemberStoreAccess> accesses = memberStoreAccessDao.getActiveAccessByMemberId(memberId);
        Map<String, String> map = new HashMap<>();
        for (MemberStoreAccess access : accesses) {
            map.put(access.getStoreId(), access.getRole().name());
        }
        return map;
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
