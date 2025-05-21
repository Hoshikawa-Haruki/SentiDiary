/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.SentiDiary.Repository;

/**
 *
 * @author Haruki
 */
import deu.se.SentiDiary.Entity.SummaryTag;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface SummaryTagRepository extends JpaRepository<SummaryTag, Long> {
}
