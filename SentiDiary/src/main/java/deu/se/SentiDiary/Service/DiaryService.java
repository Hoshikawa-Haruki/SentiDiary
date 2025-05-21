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
import deu.se.SentiDiary.Entity.Weather;
import deu.se.SentiDiary.Repository.DiaryRepository;
import deu.se.SentiDiary.Repository.EmotionTagRepository;
import deu.se.SentiDiary.Repository.SummaryTagRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DiaryService {

//    private final DiaryRepository diaryRepository;
//
//    public DiaryService(DiaryRepository diaryRepository) {
//        this.diaryRepository = diaryRepository;
//    } // 생성자 주입방식
    @Autowired
    DiaryRepository diaryRepository;
    @Autowired
    EmotionTagRepository emotionTagRepository;
    @Autowired
    SummaryTagRepository summaryTagRepository;

    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    // 일기 생성
    public void createDiary(DiaryRequest dto, Weather weather) {
        Diary diary = new Diary();
        diary.setUserId(dto.getUserId());
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setViewScope(dto.getViewScope());
        diary.setWeather(weather);
        diary.setLatitude(dto.getLatitude());
        diary.setLongitude(dto.getLongitude());
        diary.setCreatedAt(LocalDateTime.now());
        diary.setUpdatedAt(LocalDateTime.now());

        // 1. 감정 태그 연결 (다대다 관계)
        if (dto.getEmotionTagIds() != null) {  // 감정 태그 ID 목록이 null이 아닌 경우에만 처리
            Set<EmotionTag> emotionTags = dto.getEmotionTagIds().stream() // 리스트를 스트림으로 변환
                    .map(id
                            -> // 감정 태그 ID로 DB에서 조회. 없으면 예외 발생
                            emotionTagRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 감정 태그"))
                    )
                    .collect(Collectors.toSet());  // 결과를 Set으로 수집해서 중복 제거 + 저장

            diary.setEmotionTags(emotionTags);  // Diary에 감정 태그 연결
        }

        // 2. 요약 태그 생성 및 연결 (일대다 관계)
        if (dto.getSummaryKeywords() != null) { // null이 아닌 경우만
            List<SummaryTag> summaryTags = dto.getSummaryKeywords().stream()
                    .distinct() // 중복제거
                    .map(content -> {
                        SummaryTag tag = new SummaryTag();
                        tag.setContent(content);
                        tag.setDiary(diary);  // ★ 중요
                        return tag;
                    })
                    .collect(Collectors.toList());

            diary.setSummaryTags(summaryTags); // ★ 반드시 설정
        }

        diaryRepository.save(diary); // Cascade.ALL 덕분에 SummaryTag도 저장
    }
    // 일기 수정

    public void updateDiary(Long id, DiaryRequest dto, Weather weather) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diary not found"));

        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setViewScope(dto.getViewScope());
        diary.setWeather(weather);
        diary.setUpdatedAt(LocalDateTime.now());

        diaryRepository.save(diary);
    }

    // 전체 일기 조회 (관리자)
    public List<DiaryResponse> getAllDiaries() {
        return diaryRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 전체 일기 조회 (사용자 ID 기준)
    public List<DiaryResponse> getDiariesByUserId(String userId) {
        return diaryRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // DTO 직렬화 : Java 객체(DiaryResponse 등)를 JSON 문자열로 변환하는 과정
    private DiaryResponse convertToResponse(Diary diary) {
        DiaryResponse dto = new DiaryResponse();
        dto.setId(diary.getId());
        dto.setUserId(diary.getUserId());
        dto.setTitle(diary.getTitle());
        dto.setContent(diary.getContent());
        dto.setViewScope(diary.getViewScope());
        dto.setCreatedAt(diary.getCreatedAt().toString());
        dto.setUpdatedAt(diary.getUpdatedAt().toString());
        if (diary.getWeather() != null) {
            dto.setWeatherName(diary.getWeather().getName());
        }
        return dto;
    }

    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }
}
