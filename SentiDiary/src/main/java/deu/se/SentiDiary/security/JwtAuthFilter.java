/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.security;

import deu.se.SentiDiary.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 인증 필터 매 요청마다 Authorization 헤더에 포함된 JWT를 검사하고, 유효한 경우 SecurityContext에 인증
 * 정보를 설정해줍니다. 유효하지 않은 경우 401 에러 반환
 *
 * @author Haruki
 */
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후 부분

            try {
                // 2. 토큰 검증 및 사용자 정보 추출
                Claims claims = jwtUtil.validateToken(token);
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                // 3. 권한 객체 생성
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userId, null, List.of(authority));

                // 4. SecurityContext에 저장 (인증 완료)
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException e) {
                // 토큰 유효하지 않음
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT");
                return;
            }
        }

        // 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
