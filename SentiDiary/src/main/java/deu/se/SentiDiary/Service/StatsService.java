/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

import deu.se.SentiDiary.Entity.Diary;
import deu.se.SentiDiary.Entity.EmotionTag;
import deu.se.SentiDiary.Entity.SummaryTag;
import deu.se.SentiDiary.Repository.DiaryRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 06.04 통계 기능 클래스
 * 사용자의 전체/월별 감정 통계
 * 사용자의 전체/월별 요약 통계
 *
 * @author Haruki
 */
@Slf4j
@Service
public class StatsService {

    @Autowired
    private DiaryRepository diaryRepository;
    // 고정된 감정 키워드 리스트 (한글)
    private static final List<String> EMOTION_KR_LIST = List.of(
            "기쁨", "행복", "설렘", "화남", "우울함", "슬픔", "지루함", "놀람", "불안", "부끄러움"
    );

    // 사용자의 전체 감정 통계 (한글 + 0 포함)
    public Map<String, Long> getEmotionStatsByUser(String userId) {
        log.info("[감정 통계 요청] userId={}", userId);
        List<Diary> diaries = diaryRepository.findByUserUserid(userId);

        // 0으로 초기화
        Map<String, Long> stats = EMOTION_KR_LIST.stream()
                .collect(Collectors.toMap(e -> e, e -> 0L));

        for (Diary diary : diaries) {
            for (EmotionTag tag : diary.getEmotionTags()) {
                String emotion = tag.getName();
                if (stats.containsKey(emotion)) {
                    stats.put(emotion, stats.get(emotion) + 1);
                } else {
                    log.warn("정의되지 않은 감정 태그: {}", emotion);
                }
            }
        }

        log.info("최종 감정 통계 결과: {}", stats);
        return stats; //Map<String, Long>을 ResponseEntity로 리턴하면, 자동으로 JSON으로 변환
    }

    // DiaryRepository : 9. 사용자의 월별 통계 [감정]
    public Map<String, Long> getMonthlyEmotionStats(String userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Diary> diaries = diaryRepository.findByUserUseridAndDiaryDateBetween(userId, start, end);
        log.info("[{}년 {}월 감정 통계 요청] 일기 수: {}", year, month, diaries.size());

        Map<String, Long> stats = EMOTION_KR_LIST.stream()
                .collect(Collectors.toMap(e -> e, e -> 0L));

        for (Diary diary : diaries) {
            for (EmotionTag tag : diary.getEmotionTags()) {
                String emotion = tag.getName();
                if (stats.containsKey(emotion)) {
                    stats.put(emotion, stats.get(emotion) + 1);
                } else {
                    log.warn("정의되지 않은 감정 태그: {}", emotion);
                }
            }
        }

        log.info("최종 월별 감정 통계 결과: {}", stats);
        return stats;
    }

    // 사용자의 전체 요약 통계
    public Map<String, Integer> getUserTagStats(String userId) {
        log.info("[요약 태그 통계 요청] userId={}", userId);
        List<Diary> diaries = diaryRepository.findByUserUserid(userId);
        Map<String, Integer> tagCounts = new HashMap<>();

        for (Diary diary : diaries) {
            List<SummaryTag> tags = diary.getSummaryTags(); // SummaryTag 객체 리스트

            if (tags != null) {
                for (SummaryTag tag : tags) {
                    String tagName = tag.getContent();
                    tagCounts.put(tagName, tagCounts.getOrDefault(tagName, 0) + 1);
                }
            }
        }

        return tagCounts;
    }

    // DiaryRepository : 9. 사용자의 월별 통계 [요약]
    public Map<String, Integer> getMonthlyTagStats(String userId, int year, int month) {
        // 해당 연/월의 시작일 (예: 2025-06-01)
        LocalDate start = LocalDate.of(year, month, 1);
        // 해당 연/월의 마지막일 (예: 2025-06-30, 윤년이면 2월 29일 등도 자동 처리됨)
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Diary> diaries = diaryRepository.findByUserUseridAndDiaryDateBetween(userId, start, end);
        log.info("[{}년 {}월 태그 통계 요청] 일기 수: {}", year, month, diaries.size());

        // 태그별 출현 횟수를 저장할 맵 (태그명 -> 빈도수)
        Map<String, Integer> tagCounts = new HashMap<>();

        for (Diary diary : diaries) {
            List<SummaryTag> tags = diary.getSummaryTags();
            if (tags != null) {
                for (SummaryTag tag : tags) {
                    String tagName = tag.getContent();
                    tagCounts.put(tagName, tagCounts.getOrDefault(tagName, 0) + 1);
                }
            }
        }

        log.info("최종 월별 태그 통계 결과: {}", tagCounts);
        return tagCounts;
    }
}
