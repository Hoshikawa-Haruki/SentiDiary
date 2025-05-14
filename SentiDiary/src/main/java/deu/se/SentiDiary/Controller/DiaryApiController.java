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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/create") // → POST /api/diary/create
    public ResponseEntity<String> createDiary(@RequestBody DiaryRequest dto) {
        try {
            Weather weather = weatherService.getById(dto.getWeatherId());
            diaryService.createDiary(dto, weather);
            return ResponseEntity.status(201).body("일기 저장 성공"); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(400).body("일기 저장 실패: " + e.getMessage());
        }
    }

    // 관리자용 전체 조회
    @GetMapping("/admin")
    public ResponseEntity<List<DiaryResponse>> getAllDiaries() {
        return ResponseEntity.ok(diaryService.getAllDiaries());
    }

    // 사용자용 userid 기준 조회
    @GetMapping("/userid")
    public ResponseEntity<List<DiaryResponse>> getUserDiaries(@RequestParam String userId) {
        List<DiaryResponse> diaries = diaryService.getDiariesByUserId(userId);
        return ResponseEntity.ok(diaries);
    }
}
