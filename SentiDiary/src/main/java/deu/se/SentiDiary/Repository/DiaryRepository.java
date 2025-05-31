/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Repository;

/**
 * 05.30 weather 다대다 테이블 제거
 * @author Haruki
 */
import deu.se.SentiDiary.Entity.Diary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    // 커스텀 쿼리 메서드 추가 가능
    List<Diary> findByUserId(String userId);
}
