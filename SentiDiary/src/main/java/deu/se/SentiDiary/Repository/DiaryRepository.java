/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Repository;

/**
 * 전체일기 : all 단건일기 : 1개 특정일기 : 일부
 *
 * @author Haruki
 */
import deu.se.SentiDiary.Entity.Diary;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    // 1. 사용자의 전체일기 아이디 기준 조회
    // StatsService 클래스에서 사용
    List<Diary> findByUserUserid(String userId);

    // 2. 사용자의 전체일기 아이디 기준 최신순 조회
    List<Diary> findByUserUseridOrderByDiaryDateDesc(String userId);

    // 3. 사용자의 단건일기 아이디 기준 조회
    Optional<Diary> findByIdAndUserUserid(Long id, String userId);

    // 4. 사용자의 특정일기 아이디+날짜 기준 최신순 조회
    List<Diary> findByUserUseridAndDiaryDateOrderByUpdatedAtDesc(String userId, LocalDate diaryDate);

    // 5. 들춰보기 단건일기 조회
    @Query(value = "SELECT * FROM diary WHERE view_scope = true ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Diary> findRandomPublicDiary();

    // 6. 사용자의 제목 기준 검색
    List<Diary> findByUserUseridAndTitleContaining(String userId, String keyword);

    // 7. 사용자의 내용 기준 검색
    List<Diary> findByUserUseridAndContentContaining(String userId, String keyword);

    // 8. 사용자의 제목+내용 기준 검색
    List<Diary> findByUserUseridAndTitleContainingOrContentContaining(String userId, String keyword1, String keyword2);

    // 9. 사용자의 월별 통계 [감정, 요약]
    List<Diary> findByUserUseridAndDiaryDateBetween(String userId, LocalDate start, LocalDate end);

    // 10. 사용자의 감정태그 기준 검색
    List<Diary> findByUserUseridAndEmotionTagsNameContaining(String userId, String tagName);

    // 11. 사용자의 요약태그 기준 검색
    List<Diary> findByUserUseridAndSummaryTagsContentContaining(String userId, String tagName);

    // 12. 사용자의 일기 갯수 조회
    long countByUserUserid(String userId);

    // 13. 7일간 일기 통계
    @Query("SELECT d.diaryDate, COUNT(d) FROM Diary d WHERE d.diaryDate >= :startDate GROUP BY d.diaryDate ORDER BY d.diaryDate")
    List<Object[]> countDailyDiariesSince(@Param("startDate") LocalDate startDate);

    // 14. 주간 일기 통계
    @Query("SELECT FUNCTION('YEARWEEK', d.diaryDate, 1) AS week, COUNT(d.id) "
            + "FROM Diary d GROUP BY week ORDER BY week")
    List<Object[]> countByWeek();

    // 15. 월간 일기 통계
    @Query("SELECT FUNCTION('DATE_FORMAT', d.diaryDate, '%Y-%m') as month, COUNT(d.id) "
            + "FROM Diary d GROUP BY month ORDER BY month")
    List<Object[]> countByMonth();

}
