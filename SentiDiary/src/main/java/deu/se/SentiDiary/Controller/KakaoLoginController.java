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

/*
* 06.09 카카오 인증/회원가입 처리 클래스
* 플러터에서 카카오 로그인 누름 -> /login 호출 -> 카카오에서 /kakao/callback으로 리다이렉트
 */
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
    // 사용자를 카카오 로그인 페이지(https://kauth.kakao.com/oauth/authorize)로 보냄.
    // 카카오에 로그인 후, **인가 코드 (authorization code)**를 발급받기 위함.
    // 리다이렉트 URI를 통해 카카오는 로그인 후 본 서버의 /callback으로 자동 리디렉션
    @GetMapping("/login")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
        response.sendRedirect(kakaoLoginUrl);
    }

    // 2. 콜백 처리 (카카오측에서 호출)
    //카카오가 리디렉트하면서 넘긴 code를 파라미터로 받음
    //이 code를 이용해 Kakao 서버에서 액세스 토큰을 요청함
    //이후 Kakao API를 통해사용자 정보를 가져옴.
    //서버 내에서 회원가입 또는 로그인 처리 수행
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
