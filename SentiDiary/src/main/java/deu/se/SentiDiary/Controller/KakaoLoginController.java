package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.DTO.KakaoLoginResponse;
import deu.se.SentiDiary.Service.KakaoLoginService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
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
//    @GetMapping("/callback")
//    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) throws Exception {
//        return kakaoLoginService.handleKakaoLogin(code);
//    }

    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
        KakaoLoginResponse result = kakaoLoginService.handleKakaoLogin(code); // 내부적으로 토큰 + userId + nickname 만들어짐

        // 06.06 Flutter 앱으로 redirect
        String redirectUrl = "Senti://kakao_login" // 스키마 스킴 주소
                + "?token=" + URLEncoder.encode(result.getToken(), "UTF-8")
                + "&nickname=" + URLEncoder.encode(result.getNickname(), "UTF-8");

        response.sendRedirect(redirectUrl);
    }
}
