/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.DTO.DiaryRequest;
import deu.se.SentiDiary.DTO.DiaryResponse;
import deu.se.SentiDiary.Service.DiaryService;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 06.06 일기 CRUD 요청 관리 클래스
 *
 * @author Haruki
 */
@Slf4j
@RestController
@RequestMapping("/api/diary") // 요청(ex : POST) + /api/diary/
public class DiaryApiController {

    @Autowired
    private DiaryService diaryService;

    // 1. 일기 작성 [유저]
    @PostMapping
    public ResponseEntity<String> createDiary(@RequestBody DiaryRequest dto) {
        try {
            // JWT 토큰에서 userId 추출
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰 파싱
            dto.setUserId(userId); // 서버에서 직접 userId 설정

            diaryService.createDiary(dto);
            return ResponseEntity.status(201).body("일기 저장 성공"); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(400).body("일기 저장 실패: " + e.getMessage());
        }
    }

    // 2. 일기 수정 [유저]
    @PutMapping("/{id}") // diaryid
    public ResponseEntity<String> updateDiary(@PathVariable Long id, @RequestBody DiaryRequest dto) {
        try {
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰 파싱
            dto.setUserId(userId); // 서버에서 직접 userId 설정

            diaryService.updateDiary(id, dto);
            return ResponseEntity.ok("일기 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("일기 수정 실패: " + e.getMessage());
        }
    }

    // 3. 일기 삭제 [유저, 관리자]
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiary(@PathVariable Long id) {
        log.info("[일기 삭제 요청] diaryId={}", id);
        try {
            diaryService.deleteDiary(id);
            return ResponseEntity.ok("일기 삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("일기 삭제 실패: " + e.getMessage());
        }
    }

    // 전체일기 조회 (관리자) [관리자]
    @GetMapping
    public ResponseEntity<List<DiaryResponse>> getAllDiaries() {
        log.info("[전체 일기 조회 요청 - 관리자]");
        return ResponseEntity.ok(diaryService.getAllDiaries());
        //ResponseEntity.ok : 성공 응답(200)코드와 본문(JSON) 반환
    }

    // DiaryRepository : 2. 사용자의 전체일기 아이디 기준 최신순 조회
    @GetMapping("/my")
    public ResponseEntity<List<DiaryResponse>> getDiariesByUserIdAndDateDesc() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰 파싱
        return ResponseEntity.ok(diaryService.getDiariesByUserIdAndDateDesc(userId));
    }

    // DiaryRepository : 3. 사용자 단건일기 아이디 기준 조회
    @GetMapping("/my/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> getMyDiaryById(@PathVariable Long diaryId) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰 파싱
        return ResponseEntity.ok(diaryService.getDiaryByUserIdAndDiaryId(userId, diaryId));
    }

    // DiaryRepository : 4. 사용자의 특정일기 아이디+날짜 기준 최신순 조회
    @GetMapping("/my/date/{diaryDate}")
    public ResponseEntity<List<DiaryResponse>> getMyDiariesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaryDate) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰 파싱
        List<DiaryResponse> diaries = diaryService.getDiariesByDateDesc(userId, diaryDate);
        return ResponseEntity.ok(diaries);
    }

    // DiaryRepository : 5. 들춰보기 단건일기 조회
    @GetMapping("/random")
    public ResponseEntity<DiaryResponse> getRandomPublicDiary() {
        DiaryResponse diary = diaryService.getAnyPublicDiary();
        return ResponseEntity.ok(diary);
    }
}
