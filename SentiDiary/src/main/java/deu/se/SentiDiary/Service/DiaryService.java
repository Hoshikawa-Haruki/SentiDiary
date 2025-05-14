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
import deu.se.SentiDiary.Entity.Weather;
import deu.se.SentiDiary.Repository.DiaryRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

//    public List<Diary> getAllDiaries() {
//        return diaryRepository.findAll();
//    }
    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    // ✅ Flutter용 생성
    public void createDiary(DiaryRequest dto, Weather weather) {
        Diary diary = new Diary();
        diary.setUserId(dto.getUserId());
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setViewScope(dto.getViewScope());
        diary.setWeather(weather);
        diary.setCreatedAt(LocalDateTime.now());
        diary.setUpdatedAt(LocalDateTime.now());

        diaryRepository.save(diary);
    }

    // ✅ Flutter용 수정
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
