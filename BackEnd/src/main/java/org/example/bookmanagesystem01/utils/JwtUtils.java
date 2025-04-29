package org.example.bookmanagesystem01.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class JwtUtils {
    // 从配置文件中读取密钥
    @Value("${jwt.secret}")
    private String secretKey;

    // 令牌过期时间，单位为毫秒，这里设置为 1 小时
    private static final long JWT_TOKEN_VALIDITY = 60 * 60 * 1000;

    // 生成签名密钥
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 从令牌中获取用户名
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 从令牌中获取过期时间
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 从令牌中获取指定的声明
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 解析令牌获取所有声明
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查令牌是否过期
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 生成令牌
    public String generateToken(UserDetails userDetails, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", userType);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // 实际生成令牌的方法
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 验证令牌
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}    