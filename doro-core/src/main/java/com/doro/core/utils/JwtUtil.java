package com.doro.core.utils;

import com.doro.core.properties.GlobalProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具包
 */
@Component
public class JwtUtil {

    /**
     * 生成token
     *
     * @param user 用户信息
     * @return token
     */
    public static String generate(String user) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(user)
                .expiration(generateExpiration())
                .issuedAt(new Date())
                .compressWith(Jwts.ZIP.DEF)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 获取到期时间
     *
     * @return 到期时间
     */
    private static Date generateExpiration() {
        return new Date(System.currentTimeMillis() + GlobalProperties.JWT_EXPIRED * 1000);
    }

    /**
     * 获取token中的信息
     *
     * @param token token
     * @return token中的信息
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey()).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(GlobalProperties.JWT_SECRET.getBytes());
    }
}
