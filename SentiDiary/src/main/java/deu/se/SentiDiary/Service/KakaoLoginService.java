/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import deu.se.SentiDiary.DTO.KakaoLoginResponse;
import deu.se.SentiDiary.Entity.User;
import deu.se.SentiDiary.Repository.UserRepository;
import deu.se.SentiDiary.util.JwtUtil;
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
 * 06.06 카카오 인증 및 jwt 발행 클래스
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

    @Autowired
    private JwtUtil jwtUtil;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public KakaoLoginResponse handleKakaoLogin(String code) throws Exception {
        // 1) 액세스 토큰 요청
        String accessToken = getAccessToken(code);

        // 2) 사용자 정보 요청
        JsonNode profileJson = getUserProfile(accessToken);
        log.info("[INFO] 카카오 사용자 정보 JSON: {}", profileJson.toPrettyString());

        String kakaoId = profileJson.get("id").asText();
        String nickname = profileJson.path("properties").path("nickname").asText();
        String userId = "kakao_" + kakaoId; // 카카오_고유번호 형식

        // 3) DB 조회 및 자동 회원가입 처리
        User user = userRepository.findByUserid(userId).orElseGet(() -> {
            log.info("[신규 사용자] DB에 회원 정보가 없어서 자동 가입 진행 → {}", userId);
            User newUser = new User();
            newUser.setUserid(userId);
            newUser.setNickname(nickname);
            newUser.setRole("USER");
            return userRepository.save(newUser);
        });

        // 4) JWT 발급
        String role = user.getRole();  // DB에서 실제 권한 가져오기
        String token = jwtUtil.createToken(userId, role); // 토큰 생성
        log.info("[토큰 발급 완료] userId={}, role={}, tokenPreview={}...", userId, role, token.substring(0, 10));
        
        // 5) 응답객체 생성해서 반환
        return new KakaoLoginResponse(token, nickname);
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
