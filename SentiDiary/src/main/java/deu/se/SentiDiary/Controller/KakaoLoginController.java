package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.Repository.UserRepository;
import deu.se.SentiDiary.Service.KakaoLoginService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequestMapping("/kakao")
public class KakaoLoginController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    
    @Autowired
    private KakaoLoginService kakaoLoginService;

    // 1. 카카오 로그인 페이지로 리다이렉트
    // 호출 시 자동으로 카카오측에서 /kakao/callback 으로 브라우저 리다이렉트
    @GetMapping("/login")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
        response.sendRedirect(kakaoLoginUrl);
    }
    
    // 2. 콜백 처리
    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam("code") String code, HttpSession session) throws Exception {
        return kakaoLoginService.handleKakaoLogin(code, session);
    }
}
