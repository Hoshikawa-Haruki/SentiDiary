/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.Service.StatsService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Haruki
 */
@Slf4j
@RestController
@RequestMapping("/api/diary/stats")
public class DiaryStatsController {

    @Autowired
    private StatsService statsService;

    // 06.07 전체 감정 통계. 토큰기반으로 수정 필요
    @GetMapping("/emotion")
    public ResponseEntity<Map<String, Long>> getEmotionStats() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰에서 userId 추출
        return ResponseEntity.ok(statsService.getEmotionStatsByUser(userId));
    }
    
    // 사용자의 전체 요약 통계
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Integer>> getTagStats() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰에서 userId 추출
        // 서비스 호출
        Map<String, Integer> tagStats = statsService.getUserTagStats(userId);
        return ResponseEntity.ok(tagStats);
    }

    // DiaryRepository : 7. 사용자의 월별 감정 통계
    @GetMapping("/emotion/{year}/{month}")
    public ResponseEntity<Map<String, Long>> getMonthlyEmotionStats(
            @PathVariable int year,
            @PathVariable int month
    ) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰에서 userId 추출
        return ResponseEntity.ok(statsService.getMonthlyEmotionStats(userId, year, month));
    }

    // DiaryRepository : 7. 사용자의 월별 요약 통계
    @GetMapping("/summary/{year}/{month}")
    public ResponseEntity<Map<String, Integer>> getMonthlyTagStats(
            @PathVariable int year,
            @PathVariable int month
    ) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰에서 userId 추출
        Map<String, Integer> stats = statsService.getMonthlyTagStats(userId, year, month);
        return ResponseEntity.ok(stats);
    }
}
