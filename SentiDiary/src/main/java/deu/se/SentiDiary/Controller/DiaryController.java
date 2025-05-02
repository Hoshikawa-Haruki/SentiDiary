/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

/**
 *
 * @author Haruki
 */
import deu.se.SentiDiary.Model.Diary;
import deu.se.SentiDiary.Service.DiaryService;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/diaries")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    // 다이어리 전체 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<Diary>> getAllDiaries() {
        List<Diary> diaries = diaryService.getAllDiaries(); // 리스트 형태로 일기 객체들 반환
        if (diaries == null || diaries.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // 널이면 빈 리스트를 반환
        }
        return ResponseEntity.ok(diaries);
    }

    // 다이어리 작성
    @PostMapping("/createDiary")
    public ResponseEntity<Diary> createDiary(@ModelAttribute Diary diaryRequest) {
        log.info("createDiary is called...");
        diaryRequest.setId(null); // 반드시 null로 설정해서 auto-increment 유도
        Diary createdDiary = diaryService.createDiary(diaryRequest);
        return ResponseEntity.status(201).body(createdDiary); // 생성된 Diary 객체를 JSON으로 반환
    }

//    @PostMapping("/createDiary") // 응답이 필요 없으면 이렇게 해도 됨
//    public void createDiary(@ModelAttribute Diary diaryRequest) {
//        diaryService.createDiary(diaryRequest);
//    }

    // 다이어리 개별 조회
    @GetMapping("/{id}")
    public ResponseEntity<Diary> getDiaryById(@PathVariable Long id) { // url id값을 매핑
        Optional<Diary> optionalDiary = diaryService.getDiaryById(id); // 결과가 null 일수도 있다는 뜻
        if (optionalDiary.isEmpty()) { // 널이면
            return ResponseEntity.status(404).body(null); // 에러처리
        }
        Diary diary = optionalDiary.get(); // 객체 할당
        return ResponseEntity.ok(diary); // 반환
    }

    // 다이어리 수정
    @PutMapping("/{id}")
    public ResponseEntity<Diary> updateDiary(@PathVariable Long id, @RequestBody Diary diaryRequest) {
        Diary updatedDiary = diaryService.updateDiary(id, diaryRequest);
        return ResponseEntity.ok(updatedDiary);
    }

    // 다이어리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiary(@PathVariable Long id) {
        diaryService.deleteDiary(id);
        return ResponseEntity.ok("삭제 되었습니다.");
    }
}
