/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.DTO;

import java.util.List;

/**
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
    private String weatherName; // weather 엔티티에서 추출
    private List<String> emotionTags;
    private List<String> summaryTags;
}
