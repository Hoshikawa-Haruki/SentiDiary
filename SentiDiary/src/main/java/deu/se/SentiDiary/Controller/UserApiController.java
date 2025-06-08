/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Autowired
    private UserService userService;

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰에서 userId 파싱
        userService.deleteUser(userId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
