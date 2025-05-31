/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.DTO;

import java.util.List;

/**
 * 서버에서 처리한 일기 정보를 클라이언트에게 JSON으로 응답할 때 사용 (GET 응답 등)
 *
 * @author Haruki
 */
public class DiaryResponse {

    private Long id;
    private String userId;
    private String title;
    private String content;
    private Boolean viewScope;
    private String createdAt;
    private String updatedAt;
    private int weatherId;  // 숫자형으로 변경
    private List<Long> emotionTagIds;
    private List<String> summaryKeywords;
    private Double latitude;
    private Double longitude;

    // Getters & Setters
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

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public Boolean getViewScope() {
        return viewScope;
    }

    public void setViewScope(Boolean viewScope) {
        this.viewScope = viewScope;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Long> getEmotionTagIds() {
        return emotionTagIds;
    }

    public void setEmotionTagIds(List<Long> emotionTagIds) {
        this.emotionTagIds = emotionTagIds;
    }

    public List<String> getSummaryKeywords() {
        return summaryKeywords;
    }

    public void setSummaryKeywords(List<String> summaryKeywords) {
        this.summaryKeywords = summaryKeywords;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
}
