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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.DynamicInsert;

@Entity // JPA 엔티티 클래스
@DynamicInsert  // null이거나 디폴트 컬럼은 insert SQL에 안 들어감
@Table(name = "diary")
public class Diary {

    @Id // 기본키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 증가 설정 (db의 increment)
    @Column(name = "id")
    private Long id; // 일기 id

    @Column(nullable = false) // DB 컬럼으로 매핑, NOT NULL
    private String userId; // 작성자 id

    @Column(nullable = false, length = 255) // 도메인 설정
    private String title; // 제목

    @Column(nullable = false, columnDefinition = "TEXT") // 도메인 설정2
    private String content; // 내용

    @Column(nullable = false)
    private Boolean viewScope; // 공개범위

    @ManyToOne
    @JoinColumn(name = "weather_id")  // 날씨, 외래 키
    private Weather weather;  // ✅ 객체 참조로 바꿈

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 작성시간

    @Column
    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정시간

    @ManyToMany // 다대다
    @JoinTable( // 조인테이블
            name = "diary_emotion", // 중간테이블
            joinColumns = @JoinColumn(name = "diary_id"), // 현재 엔티티(Diary)의 FK가 뭐냐를 지정
            inverseJoinColumns = @JoinColumn(name = "emotion_id") //상대 엔티티(EmotionTag)의 FK가 뭐냐를 지정
    )
    private Set<EmotionTag> emotionTags = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "diary_summary",
            joinColumns = @JoinColumn(name = "diary_id"),
            inverseJoinColumns = @JoinColumn(name = "summary_id")
    )
    // private Set<SummaryTag> summaryTags = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getViewScope() {
        return viewScope;
    }

    public void setViewScope(Boolean viewScope) {
        this.viewScope = viewScope;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

//    public Set<Emotion> getEmotionTags() {
//        return emotionTags;
//    }
//
//    public void setEmotionTags(Set<Emotion> emotionTags) {
//        this.emotionTags = emotionTags;
//    }
// Getters and Setters
}
