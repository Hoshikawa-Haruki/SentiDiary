/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

import deu.se.SentiDiary.Entity.User;
import deu.se.SentiDiary.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Haruki
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(HttpSession session) {
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

        return "redirect:/index";  // 메인 페이지로 이동
    }

}
