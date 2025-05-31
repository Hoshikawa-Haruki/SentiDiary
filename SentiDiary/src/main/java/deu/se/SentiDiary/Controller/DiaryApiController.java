/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.DTO.DiaryRequest;
import deu.se.SentiDiary.DTO.DiaryResponse;
import deu.se.SentiDiary.Service.DiaryService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 1. 일기 작성
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

    // 2. 일기 수정
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

    // 3. 전체 일기 조회 (관리자)
    // GET /api/diaries → 전체 일기 조회 (관리자)
    @GetMapping
    public ResponseEntity<List<DiaryResponse>> getAllDiaries() {
        log.info("[전체 일기 조회 요청 - 관리자]");
        return ResponseEntity.ok(diaryService.getAllDiaries());
        //ResponseEntity.ok : 성공 응답(200)코드와 본문(JSON) 반환
    }

    // 5. 전체 일기 날짜순 desc 조회 (사용자 ID 기준)
    // GET /api/users/{userId}/diaries
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<DiaryResponse>> getDiariesByUserIdAndDateDesc(@PathVariable String userId) {
        return ResponseEntity.ok(diaryService.getDiariesByUserIdAndDateDesc(userId));
    }

    // 6. 일기 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiary(@PathVariable Long id) {
        try {
            diaryService.deleteDiary(id);
            return ResponseEntity.ok("일기 삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("일기 삭제 실패: " + e.getMessage());
        }
    }
}
