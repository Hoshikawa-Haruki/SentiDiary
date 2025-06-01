/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import deu.se.SentiDiary.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Haruki
 */
@Slf4j
@Service
public class KakaoLoginService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    
    @Autowired
    private UserRepository userRepository;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String handleKakaoLogin(String code, HttpSession session) throws Exception {
        // 1) 액세스 토큰 요청
        String accessToken = getAccessToken(code);

        // 2) 사용자 정보 요청
        JsonNode profileJson = getUserProfile(accessToken);
        log.info("[INFO] 카카오 사용자 정보 JSON: {}", profileJson.toPrettyString());
        
        String kakaoId = profileJson.get("id").asText();
        String nickname = profileJson.path("properties").path("nickname").asText();
        String userId = "kakao_" + kakaoId;

        // 3) 회원 여부 확인 및 세션 처리
        if (userRepository.findByUserid(userId).isPresent()) {
            log.info("기존 사용자 로그인 성공: {} (kakao_{})", nickname, kakaoId);  // 로그인 로그 유지
            session.setAttribute("userid", userId);
            session.setAttribute("nickname", nickname);
            return "loginSuccess";
        } else {
            log.info("신규 사용자 → 회원가입 페이지로 이동: {} (kakao_{})", nickname, kakaoId);  // 회원가입 전 진입 로그
            session.setAttribute("temp_userid", userId);
            session.setAttribute("temp_nickname", nickname);
            return "register";
        }
    }

    private String getAccessToken(String code) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token", request, String.class);

        return mapper.readTree(response.getBody()).get("access_token").asText();
    }

    private JsonNode getUserProfile(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity, String.class);

        return mapper.readTree(response.getBody());
    }
}
