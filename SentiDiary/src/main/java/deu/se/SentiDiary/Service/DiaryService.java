/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

/**
 *
 * @author Haruki
 */
import deu.se.SentiDiary.DTO.DiaryRequest;
import deu.se.SentiDiary.DTO.DiaryResponse;
import deu.se.SentiDiary.Entity.Diary;
import deu.se.SentiDiary.Entity.EmotionTag;
import deu.se.SentiDiary.Entity.SummaryTag;
import deu.se.SentiDiary.Repository.DiaryRepository;
import deu.se.SentiDiary.Repository.EmotionTagRepository;
import deu.se.SentiDiary.Repository.SummaryTagRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DiaryService {

    @Autowired
    DiaryRepository diaryRepository;
    @Autowired
    EmotionTagRepository emotionTagRepository;
    @Autowired
    SummaryTagRepository summaryTagRepository;

    //Optional : 일기가 없을수도 있을 경우
    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    // 1. 일기 작성
    public void createDiary(DiaryRequest dto) {
        Diary diary = new Diary();
        diary.setUserId(dto.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        diary.setDiaryDate(LocalDate.parse(dto.getDiaryDate(), formatter));
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setViewScope(dto.getViewScope());
        diary.setWeatherId(dto.getWeatherId()); // int형 weather
        diary.setLatitude(dto.getLatitude());
        diary.setLongitude(dto.getLongitude());
        diary.setCreatedAt(LocalDateTime.now());
        diary.setUpdatedAt(LocalDateTime.now());

        // 감정 태그 처리
        if (dto.getEmotionTagIds() != null) {
            Set<EmotionTag> emotionTags = dto.getEmotionTagIds().stream()
                    .map(id -> emotionTagRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 감정 태그")))
                    .collect(Collectors.toSet());
            diary.setEmotionTags(emotionTags);
        }

        // 요약 태그 처리
        if (dto.getSummaryKeywords() != null) {
            List<SummaryTag> summaryTags = dto.getSummaryKeywords().stream()
                    .distinct()
                    .map(content -> {
                        SummaryTag tag = new SummaryTag();
                        tag.setContent(content);
                        tag.setDiary(diary);
                        return tag;
                    })
                    .collect(Collectors.toList());
            diary.setSummaryTags(summaryTags);
        }

        diaryRepository.save(diary);
    }

    // 2. 일기 수정
    @Transactional
    public void updateDiary(Long id, DiaryRequest dto) {
        // 1) 기존 일기 조회
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 일기를 찾을 수 없습니다."));

        // 2) 기본 필드 수정
        diary.setUserId(dto.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        diary.setDiaryDate(LocalDate.parse(dto.getDiaryDate(), formatter));
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setViewScope(dto.getViewScope());
        diary.setWeatherId(dto.getWeatherId());
        diary.setLatitude(dto.getLatitude());
        diary.setLongitude(dto.getLongitude());
        diary.setUpdatedAt(LocalDateTime.now());  // 수정 시점만 갱신

        // 3) 감정 태그 갱신
        if (dto.getEmotionTagIds() != null) {
            Set<EmotionTag> emotionTags = dto.getEmotionTagIds().stream()
                    .map(tagId -> emotionTagRepository.findById(tagId) // json 에서 보낸 감정요약 id
                    .orElseThrow(() -> new IllegalArgumentException("감정 태그를 찾을 수 없습니다: " + tagId)))
                    .collect(Collectors.toSet());
            diary.setEmotionTags(emotionTags);
        }

        // 4) 요약 태그 갱신
        if (dto.getSummaryKeywords() != null) {
            List<SummaryTag> summaryTags = diary.getSummaryTags();
            summaryTags.clear();  // 기존 태그 모두 삭제

            for (String keyword : dto.getSummaryKeywords()) {
                SummaryTag tag = new SummaryTag();
                tag.setContent(keyword);
                tag.setDiary(diary);
                summaryTags.add(tag);  // 기존 list에 추가
            }
        }

        // 5) 저장 (선택적으로 호출 가능, JPA 변경 감지로 자동 반영되기도 함)
        diaryRepository.save(diary);
    }

    // 3. 전체 일기 조회 (관리자)
    public List<DiaryResponse> getAllDiaries() {
        return diaryRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 4. 전체 일기 조회 (사용자 ID 기준)
    public List<DiaryResponse> getDiariesByUserId(String userId) {
        return diaryRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 5. 전체 일기 날짜순 desc 조회 (사용자 ID 기준)
    public List<DiaryResponse> getDiariesByUserIdAndDateDesc(String userId) {
        return diaryRepository.findByUserIdOrderByDiaryDateDesc(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 6. 일기 삭제 (사용자 ID기준)
    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }

    // 일기 JSON화 메서드. 일기 반환시 사용
    // DTO 직렬화 : Java 객체(DiaryResponse 등)를 JSON 문자열로 변환하는 과정
    private DiaryResponse convertToResponse(Diary diary) {
        DiaryResponse dto = new DiaryResponse();
        dto.setId(diary.getId());
        dto.setUserId(diary.getUserId());
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
