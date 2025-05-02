<%-- 
    Document   : writeDiary
    Created on : 2025. 4. 28., 오전 4:10:48
    Author     : Haruki
--%>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewprot" content="width=device-width, initial-scale=1.0">
        <title>일기 작성 테스트</title>
    </head>
    <body>
        <h1>일기 작성 테스트</h1>

        <form action="${pageContext.request.contextPath}/api/v1/diaries/createDiary" method="post">
            <label>사용자 ID:</label><br>
            <input type="number" name="userId" value="20203105" required><br><br>
            <label>제목:</label><br>
            <input type="text" name="title" required><br><br>

            <label>내용:</label><br>
            <textarea name="content" rows="5" cols="30" required></textarea><br><br>

            <label>공개 여부 (true/false):</label><br>
            <input type="text" name="viewScope" value="false" required><br><br>

            <label>날씨 ID (숫자):</label><br>
            <input type="number" name="weatherId" value="1" required><br><br>

            <button type="submit">작성하기</button>
        </form>

    </body>
</html>
