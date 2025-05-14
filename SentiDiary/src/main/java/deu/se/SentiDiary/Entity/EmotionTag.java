/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Entity;

/**
 *
 * @author Haruki
 */
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "emotion")
public class EmotionTag {

    @Id
    @Column(name = "tagno")
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "emotionTags")
    private Set<Diary> diaries = new HashSet<>();

    // 기본 생성자
    public EmotionTag() {
    }

    public EmotionTag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter / Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Diary> getDiaries() {
        return diaries;
    }

    public void setDiaries(Set<Diary> diaries) {
        this.diaries = diaries;
    }
}
