/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Haruki
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        // 인증된 사용자 ID 가져오기 (JwtAuthFilter에서 set한 userId)
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("✅ JWT 인증 성공! userId: " + userId);
    }
}
