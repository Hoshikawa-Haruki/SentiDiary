/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Repository;

import deu.se.SentiDiary.Entity.EmotionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Haruki
 */
@Repository
public interface EmotionTagRepository extends JpaRepository<EmotionTag, Long> {
    // 커스텀 쿼리 메서드 추가 가능
}
