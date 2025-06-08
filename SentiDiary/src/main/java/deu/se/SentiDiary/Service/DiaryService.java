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
import deu.se.SentiDiary.Entity.User;
import deu.se.SentiDiary.Repository.DiaryRepository;
import deu.se.SentiDiary.Repository.EmotionTagRepository;
import deu.se.SentiDiary.Repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    UserRepository userRepository;
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
        User user = userRepository.findByUserid(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다: " + dto.getUserId()));

        Diary diary = new Diary();
//      diary.setUserId(dto.getUserId());
        diary.setUser(user);
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
        User user = userRepository.findByUserid(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다: " + dto.getUserId()));
//        diary.setUserId(dto.getUserId());
        diary.setUser(user);
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
        return diaryRepository.findByUserUserid(userId).stream()
                .map(diaryConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    // DiaryRepository : 2. 사용자의 전체일기 아이디 기준 최신순 조회
    public List<DiaryResponse> getDiariesByUserIdAndDateDesc(String userId) {
        return diaryRepository.findByUserUseridOrderByDiaryDateDesc(userId).stream()
                .map(diaryConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    // DiaryRepository : 3. 사용자 단건일기 아이디 기준 조회
    public DiaryResponse getDiaryByUserIdAndDiaryId(String userId, Long diaryId) {
        log.info("[단건 일기 조회 요청] userId={}, diaryId={}", userId, diaryId);
        Diary diary = diaryRepository.findByIdAndUserUserid(diaryId, userId)
                .orElseThrow(() -> new RuntimeException("해당 일기를 찾을 수 없습니다."));
        return diaryConverter.convertToResponse(diary);
    }

    // DiaryRepository : 4. 사용자의 특정일기 아이디+날짜 기준 최신순 조회
    public List<DiaryResponse> getDiariesByDateDesc(String userId, LocalDate diaryDate) {
        log.info("[날짜 기준 일기 조회 요청] userId={}, diaryDate={}", userId, diaryDate);
        List<Diary> diaries = diaryRepository.findByUserUseridAndDiaryDateOrderByUpdatedAtDesc(userId, diaryDate);
        return diaries.stream().map(diaryConverter::convertToResponse).collect(Collectors.toList());
    }

    // DiaryRepository : 5. 들춰보기
    public DiaryResponse getAnyPublicDiary() {
        log.info("[들춰보기 요청]");
        Diary diary = diaryRepository.findRandomPublicDiary()
                .orElseThrow(() -> new NoSuchElementException("공개된 일기가 없습니다."));
        return diaryConverter.convertToResponse(diary);
    }

    // DiaryRepository : 12. 사용자의 일기 갯수 조회
    public Map<String, Long> getDiaryCountPerUser(List<User> users) {
        log.info("[사용자별 일기 총 갯수 조회]");
        Map<String, Long> diaryCountMap = new HashMap<>();
        for (User user : users) {
            Long count = diaryRepository.countByUserUserid(user.getUserid());
            diaryCountMap.put(user.getUserid(), count);
        }
        return diaryCountMap;
    }

    // DiaryRepository : 13. 7일간 일기 조회
    public Map<LocalDate, Long> getDiaryCountsLast7Days() {
        LocalDate startDate = LocalDate.now().minusDays(6); // 오늘 포함 7일
        List<Object[]> results = diaryRepository.countDailyDiariesSince(startDate);

        Map<LocalDate, Long> diaryCountMap = new LinkedHashMap<>();
        for (Object[] row : results) {
            LocalDate date = (LocalDate) row[0];
            Long count = (Long) row[1];
            diaryCountMap.put(date, count);
        }

        // 누락된 날짜 0으로 채우기
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            diaryCountMap.putIfAbsent(date, 0L);
        }

        return diaryCountMap;
    }

    // DiaryRepository : 14. 주간 일기 통계
    public Map<String, Long> getDiaryCountsByWeek() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusWeeks(5); // 최근 6주 포함

        Map<String, Long> weeklyStats = new LinkedHashMap<>();

        // 6주치 주간 시작일 생성
        for (int i = 0; i < 6; i++) {
            LocalDate monday = start.plusWeeks(i).with(DayOfWeek.MONDAY);
            LocalDate sunday = monday.plusDays(6);
            String label = monday.format(DateTimeFormatter.ofPattern("MM/dd"))
                    + "~"
                    + sunday.format(DateTimeFormatter.ofPattern("MM/dd"));
            weeklyStats.put(label, 0L); // 기본값 0
        }

        // DB에서 해당 구간 일기 수 집계 (주차별)
        List<Object[]> results = diaryRepository.countByWeek(); // weekKey: "2025W23" 등

        for (Object[] row : results) {
            String rawWeek = String.valueOf(row[0]);  // 예: 202524
            Long count = (Long) row[1];

            if (rawWeek.length() == 6 && rawWeek.matches("\\d{6}")) {
                int year = Integer.parseInt(rawWeek.substring(0, 4));
                int week = Integer.parseInt(rawWeek.substring(4));

                // 주차를 날짜로 변환
                LocalDate monday = LocalDate.ofYearDay(year, 1)
                        .with(WeekFields.ISO.weekOfYear(), week)
                        .with(WeekFields.ISO.dayOfWeek(), 1); // 월요일

                LocalDate sunday = monday.plusDays(6);

                String label = monday.format(DateTimeFormatter.ofPattern("MM/dd"))
                        + "~"
                        + sunday.format(DateTimeFormatter.ofPattern("MM/dd"));

                if (weeklyStats.containsKey(label)) {
                    weeklyStats.put(label, count);
                }
            }
        }

        return weeklyStats;
    }

    // DiaryRepository : 15. 월간 일기 통계
    public Map<String, Long> getDiaryCountsByMonth() {
        List<Object[]> result = diaryRepository.countByMonth(); // 쿼리 결과
        Map<String, Long> monthlyStats = new LinkedHashMap<>();
        for (Object[] row : result) {
            monthlyStats.put((String) row[0], (Long) row[1]); // row[0] = "2025-06", row[1] = count
        }
        return monthlyStats;
    }
}
