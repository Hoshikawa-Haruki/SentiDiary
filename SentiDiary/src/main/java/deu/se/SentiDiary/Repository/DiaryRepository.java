/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Repository;

/**
 * 전체일기 : all
 * 단건일기 : 1개
 * 특정일기 : 일부
 * @author Haruki
 */
import deu.se.SentiDiary.Entity.Diary;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // 1. 사용자의 전체일기 아이디 기준 조회
    List<Diary> findByUserId(String userId);
    // 2. 사용자의 전체일기 아이디 기준 최신순 조회
    List<Diary> findByUserIdOrderByDiaryDateDesc(String userId);
    // 3. 사용자의 단건일기 아이디 기준 조회
    Optional<Diary> findByIdAndUserId(Long id, String userId);
    // 4. 사용자의 특정일기 아이디+날짜 기준 최신순 조회
    List<Diary> findByUserIdAndDiaryDateOrderByUpdatedAtDesc(String userId, LocalDate diaryDate);
    // 들춰보기 단건일기 조회
    @Query(value = "SELECT * FROM diary WHERE view_scope = true ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Diary> findRandomPublicDiary();
}
