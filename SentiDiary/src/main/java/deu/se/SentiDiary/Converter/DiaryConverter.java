/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Converter;

import deu.se.SentiDiary.DTO.DiaryResponse;
import deu.se.SentiDiary.Entity.Diary;
import deu.se.SentiDiary.Entity.EmotionTag;
import deu.se.SentiDiary.Entity.SummaryTag;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 일기의 JSON 직렬화를 담당하는 클래스
 *
 * @author Haruki
 */
@Component
public class DiaryConverter {

    // 일기 JSON화 메서드. 일기 반환시 사용
    // DTO 직렬화 : Java 객체(DiaryResponse 등)를 JSON 문자열로 변환하는 과정
    public DiaryResponse convertToResponse(Diary diary) {
        DiaryResponse dto = new DiaryResponse();
        dto.setId(diary.getId());
        // dto.setUserId(diary.getUserId());
        dto.setDiaryDate(diary.getDiaryDate().toString());
        dto.setTitle(diary.getTitle());
        dto.setContent(diary.getContent());
        dto.setViewScope(diary.getViewScope());
        dto.setCreatedAt(diary.getCreatedAt().toString());
        dto.setUpdatedAt(diary.getUpdatedAt().toString());
        dto.setWeatherId(diary.getWeatherId()); // 일기 id 반환
        dto.setLatitude(diary.getLatitude()); // 위도 & 경도
        dto.setLongitude(diary.getLongitude());

        dto.setEmotionTagIds( // 감정 태그 반환
                diary.getEmotionTags().stream()
                        .map(EmotionTag::getId)
                        .collect(Collectors.toList())
        );
        dto.setSummaryKeywords( // 요약 태그 반환
                diary.getSummaryTags().stream()
                        .map(SummaryTag::getContent)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
