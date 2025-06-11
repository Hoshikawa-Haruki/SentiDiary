/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.Converter.DiaryConverter;
import deu.se.SentiDiary.DTO.DiaryResponse;
import deu.se.SentiDiary.Entity.Diary;
import deu.se.SentiDiary.Service.SearchService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Haruki
 */
@Slf4j
@RestController
@RequestMapping("/api/diary/search")
public class DiarySearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private DiaryConverter diaryConverter;

    @GetMapping
    public ResponseEntity<List<DiaryResponse>> searchDiaries(
            @RequestParam String type,
            @RequestParam String keyword
    ) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 토큰에서 userId 추출
        List<Diary> result;

        switch (type) {
            case "title" ->
                result = searchService.searchByTitle(userId, keyword);
            case "content" ->
                result = searchService.searchByContent(userId, keyword);
            case "both" ->
                result = searchService.searchByTitleOrContent(userId, keyword);
            case "emotion" ->
                result = searchService.searchByEmotionTag(userId, keyword); // 감정 태그 검색
            case "summary" ->
                result = searchService.searchBySummaryTag(userId, keyword); // 요약 태그 검색
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }

        List<DiaryResponse> response = result.stream()
                .map(diaryConverter::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}