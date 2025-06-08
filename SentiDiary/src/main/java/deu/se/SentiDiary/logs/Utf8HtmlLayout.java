/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.logs;

import ch.qos.logback.classic.html.HTMLLayout;

/**
 * 로그파일 브라우저 호출 시 UTF-8 변환 클래스
 *
 * @author Haruki
 */
public class Utf8HtmlLayout extends HTMLLayout {

    @Override
    public String getFileHeader() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: 'Pretendard', sans-serif; background: #f9f9f9; color: #000; }
                    table.log { width: 100%; border-collapse: collapse; }
                    th, td { padding: 8px 12px; border: 1px solid #ccc; font-size: 14px; color: #222; }
                    tr:nth-child(even) { background-color: #f2f2f2; }
                    th { background-color: #222; color: #fff; }
                </style>
            </head>
            <body>
            <table class="log">
                <tr><th>시간</th><th>스레드</th><th>레벨</th><th>로거</th><th>메시지</th></tr>
        """;
    }

    @Override
    public String getFileFooter() {
        return "</table></body></html>";
    }
}
