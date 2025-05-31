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

    //Optional : 일기가 없을수도 있을 경우
    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    // 일기 생성
    public void createDiary(DiaryRequest dto) {
        Diary diary = new Diary();
        diary.setUserId(dto.getUserId());
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

    // 일기 수정
    public void updateDiary(Long id, DiaryRequest dto) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diary not found"));

        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setViewScope(dto.getViewScope());
        diary.setWeatherId(dto.getWeatherId()); // int형 반환으로 변경
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
    
    // 일기 JSON화 메서드. 일기 반환시 전부 사용됨
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

    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }
}
