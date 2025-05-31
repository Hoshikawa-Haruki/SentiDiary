/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.DTO;

import java.util.List;

/**
 * 요청용 DTO 프론트가 보기 좋은 형태로 가공한 맞춤형 응답 데이터
 *
 * 필드 이름은 JSON 키 이름과 정확히 일치해야 자동 바인딩이 됨
 *
 * @author Haruki
 */
public class DiaryRequest {

    private String userId;
    private String title;
    private String content;
    private Boolean viewScope;
    private int weatherId; // 숫자형으로 변경
    private List<Long> emotionTagIds;      // 감정 태그 ID 리스트 (기존 EmotionTag에서 선택)
    private List<String> summaryKeywords;  // 요약 키워드 문자열 리스트 (일기마다 새로 생성)
    private Double latitude;
    private Double longitude;

    public DiaryRequest() {
    }

    // Getter & Setter
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

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
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
