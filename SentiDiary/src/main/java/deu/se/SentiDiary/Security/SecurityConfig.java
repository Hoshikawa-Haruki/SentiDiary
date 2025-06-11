/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.security;

import deu.se.SentiDiary.jwt.JwtAuthFilter;
import deu.se.SentiDiary.jwt.JwtUtil;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스
 *
 * @author Haruki
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // 403 접속 오류 방지
                .requestMatchers("/", "/index", "/admin/login", "/api/kakao/**").permitAll()
                .requestMatchers("/admin_style.css").permitAll()
                // ✅ 관리자 영역 - 세션 기반 ROLE_ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // ✅ 사용자 API - JWT 기반
                .requestMatchers(HttpMethod.GET, "/api/diary").hasRole("ADMIN") // 관리자 - 전체조회용
                .requestMatchers(HttpMethod.POST, "/api/diary").hasAnyRole("USER", "ADMIN") // 일기 작성
                .requestMatchers(HttpMethod.DELETE, "/api/user").hasAnyRole("USER", "ADMIN") // 회원 탈퇴
                .requestMatchers("/api/diary/**").hasAnyRole("USER", "ADMIN") // 일기 CRUD
                .anyRequest().authenticated()
        );

        // ✅ 관리자용 세션 로그인
        http.formLogin(form -> form
                .loginPage("/") // 로그인페이지로 연결
                .loginProcessingUrl("/login.do")
                .usernameParameter("userid") // 아이디 파라미터
                .passwordParameter("passwd") // 비밀번호 파라미터
                .successHandler((request, response, authentication) -> {
                    String userid = authentication.getName();
                    HttpSession session = request.getSession();
                    session.setAttribute("userid", userid);
                    session.setAttribute("role", authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ? "ADMIN" : "USER");
                    response.sendRedirect(request.getContextPath() + "/api/admin/admin_main");
                })
                .failureHandler((request, response, exception) -> {
                    request.getSession().setAttribute("loginFailed", true);
                    response.sendRedirect(request.getContextPath() + "/");
                })
        );

        // ✅ 로그아웃
        http.logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")
        );

        // ✅ JWT 필터 등록 (API 요청 앞단에)
        http.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // ✅ 세션도 허용 (관리자용), CSRF는 비활성화
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
