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

    @GetMapping("/")
    public String index() {
        log.info("● index is called...");
        return "index";
    }
    
    // 백엔드용
    @GetMapping("/createDiary")
    public String createDiary() {
        return "createDiary";
    }
    
    @GetMapping("kakao/login")
    public String kakaoLogin(){
        return "kakaologin";
    }
}
