/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.DTO.DiaryRequest;
import deu.se.SentiDiary.DTO.DiaryResponse;
import deu.se.SentiDiary.Entity.Weather;
import deu.se.SentiDiary.Service.DiaryService;
import deu.se.SentiDiary.Service.WeatherService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Haruki
 */
@RestController
@RequestMapping("/api/diary")
public class DiaryApiController {

    private final DiaryService diaryService;
    private final WeatherService weatherService;

    public DiaryApiController(DiaryService diaryService, WeatherService weatherService) {
        this.diaryService = diaryService;
        this.weatherService = weatherService;
    }

    @PostMapping // → POST /api/diary/
    public ResponseEntity<String> createDiary(@RequestBody DiaryRequest dto) {
        try {
            Weather weather = weatherService.getById(dto.getWeatherId());
            diaryService.createDiary(dto, weather);
            return ResponseEntity.status(201).body("일기 저장 성공"); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(400).body("일기 저장 실패: " + e.getMessage());
        }
    }

    // GET /api/diaries → 전체 일기 조회 (관리자)
    @GetMapping("/diaries")
    public ResponseEntity<List<DiaryResponse>> getAllDiaries() {
        return ResponseEntity.ok(diaryService.getAllDiaries());
    }

    // GET /api/users/{userId}/diaries → 특정 사용자 일기 조회
    @GetMapping("/users/{userId}/diaries")
    public ResponseEntity<List<DiaryResponse>> getUserDiaries(@PathVariable String userId) {
        return ResponseEntity.ok(diaryService.getDiariesByUserId(userId));
    }
}
