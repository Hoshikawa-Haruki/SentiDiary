/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.DTO;

/**
 * 요청용 DTO
 * 프론트가 보기 좋은 형태로 가공한 맞춤형 응답 데이터
 * 
 * 필드 이름은 JSON 키 이름과 정확히 일치해야 자동 바인딩이 됨
 * @author Haruki
 */
public class DiaryRequest {

    private String userId;
    private String title;
    private String content;
    private Boolean viewScope;
    private Long weatherId;

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

    public Long getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Long weatherId) {
        this.weatherId = weatherId;
    }
}
