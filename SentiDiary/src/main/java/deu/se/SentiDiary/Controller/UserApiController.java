/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.Repository.UserRepository;
import deu.se.SentiDiary.Entity.User;
import jakarta.servlet.http.HttpSession;
import static java.lang.StrictMath.log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    private UserRepository userRepository;

    @PostMapping("/register_confirm")
    public String registerConfirm(HttpSession session) {
        String userId = (String) session.getAttribute("temp_userid");
        String nickname = (String) session.getAttribute("temp_nickname");

        log.info("[회원가입 요청] userid={}, nickname={}", userId, nickname);

        User newUser = new User();
        newUser.setUserid(userId);
        newUser.setNickname(nickname);
        newUser.setProfileImage(null);

        userRepository.save(newUser);
        log.info("[회원가입 완료] DB에 저장된 사용자: {}", newUser.getUserid());

        session.setAttribute("userid", userId);
        session.setAttribute("nickname", nickname);
        session.removeAttribute("temp_userid");
        session.removeAttribute("temp_nickname");

        return "redirect:${pageContext.request.contextPath}/index";
    }
}
