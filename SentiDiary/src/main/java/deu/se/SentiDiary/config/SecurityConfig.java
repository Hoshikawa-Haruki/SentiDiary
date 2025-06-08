/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.sentidiary.config;

import deu.se.SentiDiary.security.JwtAuthFilter;
import deu.se.SentiDiary.util.JwtUtil;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Spring Security 설정 클래스
 *
 * @author Haruki
 */
@Configuration
@RequiredArgsConstructor // 생성자 주입. Autowired 보다 나음
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // 403 접속 오류 방지
                .requestMatchers("/", "/index", "/admin/login", "/kakao/**", "/kakaoLoginPage").permitAll() // 로그인 관련, 카카오 요청 허용
                .requestMatchers("/admin_menu", "/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/diary").hasRole("ADMIN")
                .requestMatchers("/api/diary/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                //.requestMatchers("/**").permitAll() // 모든 접근 허용     
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
