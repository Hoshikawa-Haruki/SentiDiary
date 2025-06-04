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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
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
        log.info("[일기 작성 요청] userId={}, title={}", dto.getUserId(), dto.getTitle());
        try {
            diaryService.createDiary(dto);
            return ResponseEntity.status(201).body("일기 저장 성공"); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(400).body("일기 저장 실패: " + e.getMessage());
        }
    }

    // 2. 일기 수정 [유저]
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDiary(@PathVariable Long id, @RequestBody DiaryRequest dto) {
        log.info("[일기 수정 요청] 일기 번호={}, userId={}", id, dto.getUserId());
        try {
            diaryService.updateDiary(id, dto);
            return ResponseEntity.ok("일기 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("일기 수정 실패: " + e.getMessage());
        }
    }

    // 3. 전체 일기 조회 (관리자) [관리자]
    // GET /api/diaries → 전체 일기 조회 (관리자)
    @GetMapping
    public ResponseEntity<List<DiaryResponse>> getAllDiaries() {
        log.info("[전체 일기 조회 요청 - 관리자]");
        return ResponseEntity.ok(diaryService.getAllDiaries());
        //ResponseEntity.ok : 성공 응답(200)코드와 본문(JSON) 반환
    }

    // 5. 전체 일기 날짜순 desc 조회 (사용자 ID 기준) [유저, 관리자]
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<DiaryResponse>> getDiariesByUserIdAndDateDesc(@PathVariable String userId) {
        return ResponseEntity.ok(diaryService.getDiariesByUserIdAndDateDesc(userId));
    }

    // 6. 일기 삭제 [유저, 관리자]
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

    // 7. 사용자 일기 단건 조회 (update용)
    @GetMapping("/users/{userId}/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiaryByUserIdAndDiaryId(
            @PathVariable String userId,
            @PathVariable Long diaryId) {
        log.info("[단건 일기 조회 요청] userId={}, diaryId={}", userId, diaryId);
        return ResponseEntity.ok(diaryService.getDiaryByUserIdAndDiaryId(userId, diaryId));
    }

    // 8. 들춰보기 [유저]
    @GetMapping("/random")
    public ResponseEntity<DiaryResponse> getRandomPublicDiary() {
        log.info("[들춰보기 요청]");
        DiaryResponse diary = diaryService.getAnyPublicDiary();
        return ResponseEntity.ok(diary);
    }

    // 9. 날짜 기준 일기 조회 [유저]
    @GetMapping("/api/diary/users/{userId}/date/{diaryDate}")
    public ResponseEntity<List<DiaryResponse>> getDiariesByDate(
            @PathVariable String userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaryDate) {
        log.info("[날짜 기준 일기 조회 요청] userId={}, diaryDate={}", userId, diaryDate);
        List<DiaryResponse> diaries = diaryService.getDiariesByDate(userId, diaryDate);
        return ResponseEntity.ok(diaries);
    }

}
