/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

/**
 *
 * @author Haruki
 */

import deu.se.SentiDiary.Model.Diary;
import deu.se.SentiDiary.Repository.DiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public List<Diary> getAllDiaries() {
        return diaryRepository.findAll();
    }

    public Optional<Diary> getDiaryById(Long id) {
        return diaryRepository.findById(id);
    }

    public Diary createDiary(Diary diary) { //Diary 객체를 DB에 저장
        return diaryRepository.save(diary); 
    }

    public Diary updateDiary(Long id, Diary updatedDiary) {
        return diaryRepository.findById(id).map(diary -> {
            diary.setTitle(updatedDiary.getTitle());
            diary.setContent(updatedDiary.getContent());
            diary.setViewScope(updatedDiary.getViewScope());
            diary.setWeatherId(updatedDiary.getWeatherId());
            diary.setUpdatedAt(updatedDiary.getUpdatedAt());
            return diaryRepository.save(diary);
        }).orElseThrow(() -> new RuntimeException("Diary not found"));
    }

    public void deleteDiary(Long id) {
        diaryRepository.deleteById(id);
    }
}
