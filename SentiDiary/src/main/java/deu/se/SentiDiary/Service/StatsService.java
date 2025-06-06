/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

import deu.se.SentiDiary.Entity.Diary;
import deu.se.SentiDiary.Entity.EmotionTag;
import deu.se.SentiDiary.Repository.DiaryRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 06.04 통계 기능 클래스
 * @author Haruki
 */
@Slf4j
@Service
public class StatsService {

    @Autowired
    private DiaryRepository diaryRepository;

    // DB 한글 감정 → JSON 응답용 영어 감정 이름 매핑
    private static final Map<String, String> EMOTION_KR_TO_EN = Map.of(
        "기쁨", "joy",
        "행복", "happy",
        "설렘", "excited",
        "화남", "angry",
        "우울함", "depressed",
        "슬픔", "sad",
        "지루함", "bored",
        "놀람", "surprised",
        "불안", "nervous",
        "부끄러움", "shy"
    );

    public Map<String, Long> getEmotionStatsByUser(String userId) {
        List<Diary> diaries = diaryRepository.findByUserId(userId);

        // 영어 키 기준으로 초기화
        Map<String, Long> stats = EMOTION_KR_TO_EN.values().stream()
            .collect(Collectors.toMap(e -> e, e -> 0L));

        for (Diary diary : diaries) {
            for (EmotionTag tag : diary.getEmotionTags()) { // 감정이 없을경우 빈 Set 순회 X, 카운트 X
                String kr = tag.getName(); // 예: "기쁨"
                String en = EMOTION_KR_TO_EN.get(kr); // "joy"
                if (en != null) {
                    stats.put(en, stats.get(en) + 1); // 해당 감정 키에 1 증가
                }
            }
        }
        return stats; //Map<String, Long>을 ResponseEntity로 리턴하면, 자동으로 JSON으로 변환
    }
}