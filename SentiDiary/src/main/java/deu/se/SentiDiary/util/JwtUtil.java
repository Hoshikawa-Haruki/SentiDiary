/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 유틸리티 클래스
 *
 * 이 클래스는 JWT 토큰을 생성하고, 토큰 유효성 검증 및 클레임(Claims) 추출 기능을 제공합니다. - HS256 알고리즘을 사용하여
 * JWT 서명 - JWT에 사용자 ID 및 역할(role) 정보를 포함 - 만료 시간은 기본적으로 1시간으로 설정
 *
 * 사용 예시: String token = jwtUtil.createToken("userid123", "USER"); Claims claims
 * = jwtUtil.validateToken(token);
 *
 * @author Haruki
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    // JWT 유효 시간 (1시간)
    private final long expirationMs = 1000 * 60 * 60;

    /**
     * JWT 토큰 생성
     *
     * @param userId 사용자 ID
     * @param role 사용자 역할 (예: USER, ADMIN)
     * @return 서명된 JWT 문자열
     */
    public String createToken(String userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰 유효성 검증 및 Claims 추출
     *
     * @param token 클라이언트로부터 전달된 JWT 문자열
     * @return JWT Claims (유효하지 않으면 예외 발생)
     * @throws JwtException 토큰이 위조되었거나 만료된 경우 발생
     */
    public Claims validateToken(String token) throws JwtException {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes()); // ✅ 수정됨

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
