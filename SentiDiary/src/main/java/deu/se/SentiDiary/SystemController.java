/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Haruki
 */
@Controller
@Slf4j
public class SystemController {

    // 로그인 페이지
    @GetMapping("/")
    public String index() {
        log.info("[관리자 로그인 페이지 호출]");
        return "admin/admin_loginPage";
    }

    // 백엔드용
    @GetMapping("/createDiary")
    public String createDiary() {
        return "createDiary";
    }

    @GetMapping("/kakaoLoginPage")
    public String kakaoLogin() {
        return "kakaologin";
    }
}
