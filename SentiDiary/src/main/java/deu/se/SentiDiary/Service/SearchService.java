/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

import deu.se.SentiDiary.Entity.Diary;
import deu.se.SentiDiary.Repository.DiaryRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 검색 기능 클래스
 *
 * @author Haruki
 */
@Slf4j
@Service
public class SearchService {

    @Autowired
    private DiaryRepository diaryRepository;

    // DiaryRepository : 6. 사용자의 제목 기준 검색
    public List<Diary> searchByTitle(String userId, String keyword) {
        log.info("[검색] 제목 기준 검색 요청 - userId={}, keyword='{}'", userId, keyword);
        return diaryRepository.findByUserIdAndTitleContaining(userId, keyword);
    }

    // DiaryRepository : 7. 사용자의 내용 기준 검색
    public List<Diary> searchByContent(String userId, String keyword) {
        log.info("[검색] 내용 기준 검색 요청 - userId={}, keyword='{}'", userId, keyword);
        return diaryRepository.findByUserIdAndContentContaining(userId, keyword);
    }

    // DiaryRepository : 8. 사용자의 제목+내용 기준 검색
    public List<Diary> searchByTitleOrContent(String userId, String keyword) {
        log.info("[검색] 제목+내용 기준 검색 요청 - userId={}, keyword='{}'", userId, keyword);
        return diaryRepository.findByUserIdAndTitleContainingOrContentContaining(userId, keyword, keyword);
    }
    
    // DiaryRepository : 10. 사용자의 감정태그 기준 검색
    public List<Diary> searchByEmotionTag(String userId, String tagName) {
        log.info("[검색] 감정태그 기준 검색 요청 - userId={}, tagName='{}'", userId, tagName);
        return diaryRepository.findByUserIdAndEmotionTagsNameContaining(userId, tagName);
    }
    
    // DiaryRepository : 11. 사용자의 요약태그 기준 검색
    public List<Diary> searchBySummaryTag(String userId, String tagName) {
        log.info("[검색] 요약태그 기준 검색 요청 - userId={}, tagName='{}'", userId, tagName);
        return diaryRepository.findByUserIdAndSummaryTagsContentContaining(userId, tagName);
    }
}
