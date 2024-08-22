package com.doro.core.utils;

import cn.hutool.core.date.DateUtil;
import com.doro.cache.constant.CacheConstant;
import com.doro.cache.utils.RemoteCacheUtil;
import com.doro.common.constant.LoginConstant;
import com.doro.core.service.setting.G_Setting;
import com.doro.core.service.setting.GlobalSettingAcquire;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 *
 * @author jiage
 */
@Component
public class JwtUtil {

    /**
     * 生成 Token
     *
     * @param username 用户名
     * @param userId   用户 ID
     * @return Token
     */
    public static String generate(String username, Long userId) {
        Map<String, Long> claims = new HashMap<>(1);
        claims.put(LoginConstant.CLAIMS_USER_ID, userId);

        Date now = new Date();

        return Jwts.builder()
                .claims(claims)
                // 主题
                .subject(username)
                // 过期时间
                .expiration(generateExpiration(now))
                // 签发时间
                .issuedAt(now)
                // 压缩方式
                .compressWith(Jwts.ZIP.DEF)
                // 签名
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 生成 Token，并添加到缓存
     *
     * @param username 用户名
     * @param userId   用户 ID
     * @return Token
     */
    public static String generateAndCache(String username, Long userId) {
        String token = generate(username, userId);
        int jwtExpiration = GlobalSettingAcquire.get(G_Setting.JWT_EXPIRATION);
        RemoteCacheUtil.put(CacheConstant.JWT_PREFIX + username, token, Duration.ofSeconds(jwtExpiration));
        return token;
    }

    /**
     * Token 是否过期
     */
    public static boolean isExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    /**
     * 生成到期时间
     */
    private static Date generateExpiration(Date now) {
        return DateUtil.offsetSecond(now, GlobalSettingAcquire.get(G_Setting.JWT_EXPIRATION));
    }

    /**
     * 获取 Token 中的信息
     */
    public static Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey()).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取用户名
     */
    public static String getUsername(String token) {
        return getPayload(token).getSubject();
    }

    /**
     * 获取用户名
     */
    public static String getUsername(Claims claims) {
        return claims.getSubject();
    }

    /**
     * 获取用户 ID
     */
    public static Long getUserId(String token) {
        return getPayload(token).get(LoginConstant.CLAIMS_USER_ID, Long.class);
    }

    /**
     * 获取用户 ID
     */
    public static Long getUserId(Claims claims) {
        return claims.get(LoginConstant.CLAIMS_USER_ID, Long.class);
    }

    /**
     * 获取到期时间
     */
    public static Date getExpiration(String token) {
        return getPayload(token).getExpiration();
    }

    /**
     * 获取秘钥
     */
    private static SecretKey getSecretKey() {
        String jwtSecret = GlobalSettingAcquire.get(G_Setting.JWT_SECRET);
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
