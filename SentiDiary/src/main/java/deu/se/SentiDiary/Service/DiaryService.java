/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

/**
 *
 * @author Haruki
 */
import deu.se.SentiDiary.Converter.DiaryConverter;
import deu.se.SentiDiary.DTO.DiaryRequest;
import deu.se.SentiDiary.DTO.DiaryResponse;
import deu.se.SentiDiary.Entity.Diary;
import deu.se.SentiDiary.Entity.EmotionTag;
import deu.se.SentiDiary.Entity.SummaryTag;
import deu.se.SentiDiary.Repository.DiaryRepository;
import deu.se.SentiDiary.Repository.EmotionTagRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class DiaryService {

    @Autowired
    DiaryRepository diaryRepository;
    @Autowired
    EmotionTagRepository emotionTagRepository;
    @Autowired
    private DiaryConverter diaryConverter;

    //Optional : 일기가 없을수도 있을 경우
    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    // 1. 일기 작성
    @Transactional
    public void createDiary(DiaryRequest dto) {
        log.info("[일기 작성 요청] : {}", dto.getUserId());

        Diary diary = new Diary();
        diary.setUserId(dto.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        diary.setDiaryDate(LocalDate.parse(dto.getDiaryDate(), formatter));
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setViewScope(dto.getViewScope());
        diary.setWeatherId(dto.getWeatherId());
        diary.setLatitude(dto.getLatitude());
        diary.setLongitude(dto.getLongitude());
        LocalDateTime now = LocalDateTime.now(); // 작성 시간
        diary.setCreatedAt(now);
        diary.setUpdatedAt(now);

        // 감정 태그 처리
        if (dto.getEmotionTagIds() != null) {
            Set<EmotionTag> emotionTags = new HashSet<>();
            for (Long tagId : dto.getEmotionTagIds()) {
                EmotionTag tag = emotionTagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 감정 태그: " + tagId));
                emotionTags.add(tag);
            }
            diary.setEmotionTags(emotionTags);
        }

        // 요약 태그 처리
        if (dto.getSummaryKeywords() != null) {
            List<SummaryTag> summaryTags = new ArrayList<>();

            for (String content : dto.getSummaryKeywords()) {
                if (content == null || content.trim().isEmpty()) {
                    continue; // optional: 빈 문자열 방지
                }
                SummaryTag tag = new SummaryTag();
                tag.setContent(content);
                tag.setDiary(diary);
                summaryTags.add(tag);
            }

            diary.setSummaryTags(summaryTags);
        }

        diaryRepository.save(diary);
    }

    // 2. 일기 수정
    @Transactional
    public void updateDiary(Long id, DiaryRequest dto) {
        log.info("[일기 수정 요청] 일기 번호={}, userId={}", id, dto.getUserId());

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

        // 3) 감정 태그 갱신 (for-each로 통일)
        if (dto.getEmotionTagIds() != null) {
            Set<EmotionTag> emotionTags = new HashSet<>();
            for (Long tagId : dto.getEmotionTagIds()) {
                EmotionTag tag = emotionTagRepository.findById(tagId)
                        .orElseThrow(() -> new IllegalArgumentException("감정 태그를 찾을 수 없습니다: " + tagId));
                emotionTags.add(tag);
            }
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
            diary.setSummaryTags(summaryTags);  // 연관관계 유지
        }

        // 5) 저장 (선택적으로 호출 가능, JPA 변경 감지로 자동 반영되기도 함)
        diaryRepository.save(diary);
    }

    // 3. 일기 삭제 (사용자 ID기준)
    @Transactional
    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }

    // 전체 일기 조회 (관리자)
    public List<DiaryResponse> getAllDiaries() {
        return diaryRepository.findAll().stream()
                .map(diaryConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    // DiaryRepository : 1. 사용자의 전체일기 아이디 기준 조회
    // (06.07 기준 사용 X)
    public List<DiaryResponse> getDiariesByUserId(String userId) {
        return diaryRepository.findByUserId(userId).stream()
                .map(diaryConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    // DiaryRepository : 2. 사용자의 전체일기 아이디 기준 최신순 조회
    public List<DiaryResponse> getDiariesByUserIdAndDateDesc(String userId) {
        return diaryRepository.findByUserIdOrderByDiaryDateDesc(userId).stream()
                .map(diaryConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    // DiaryRepository : 3. 사용자 단건일기 아이디 기준 조회
    public DiaryResponse getDiaryByUserIdAndDiaryId(String userId, Long diaryId) {
        Diary diary = diaryRepository.findByIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new RuntimeException("해당 일기를 찾을 수 없습니다."));
        return diaryConverter.convertToResponse(diary);
    }

    // DiaryRepository : 4. 사용자의 특정일기 아이디+날짜 기준 최신순 조회
    public List<DiaryResponse> getDiariesByDateDesc(String userId, LocalDate diaryDate) {
        List<Diary> diaries = diaryRepository.findByUserIdAndDiaryDateOrderByUpdatedAtDesc(userId, diaryDate);
        return diaries.stream().map(diaryConverter::convertToResponse).collect(Collectors.toList());
    }

    // DiaryRepository : 5. 들춰보기
    public DiaryResponse getAnyPublicDiary() {
        Diary diary = diaryRepository.findRandomPublicDiary()
                .orElseThrow(() -> new NoSuchElementException("공개된 일기가 없습니다."));
        return diaryConverter.convertToResponse(diary);
    }

//    // 일기 JSON화 메서드. 일기 반환시 사용
//    // DTO 직렬화 : Java 객체(DiaryResponse 등)를 JSON 문자열로 변환하는 과정
//    public DiaryResponse convertToResponse(Diary diary) {
//        DiaryResponse dto = new DiaryResponse();
//        dto.setId(diary.getId());
//        // dto.setUserId(diary.getUserId());
//        dto.setDiaryDate(diary.getDiaryDate().toString());
//        dto.setTitle(diary.getTitle());
//        dto.setContent(diary.getContent());
//        dto.setViewScope(diary.getViewScope());
//        dto.setCreatedAt(diary.getCreatedAt().toString());
//        dto.setUpdatedAt(diary.getUpdatedAt().toString());
//        dto.setWeatherId(diary.getWeatherId()); // 일기 id 반환
//        dto.setLatitude(diary.getLatitude()); // 위도 & 경도
//        dto.setLongitude(diary.getLongitude());
//
//        dto.setEmotionTagIds( // 감정 태그 반환
//                diary.getEmotionTags().stream()
//                        .map(EmotionTag::getId)
//                        .collect(Collectors.toList())
//        );
//        dto.setSummaryKeywords( // 요약 태그 반환
//                diary.getSummaryTags().stream()
//                        .map(SummaryTag::getContent)
//                        .collect(Collectors.toList())
//        );
//        return dto;
//    }
}
