<%-- 
    Document   : showDiary
    Created on : 2025. 5. 3., 오전 4:39:06
    Author     : Haruki
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>

        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewprot" content="width=device-width, initial-scale=1.0">
        <title>일기 목록</title>
    </head>
    <body>
        <h2>📝 작성된 일기 목록</h2>

        <table border="1" cellpadding="8" cellspacing="0">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>작성자 ID</th>
                    <th>제목</th>
                    <th>내용</th>
                    <th>공개 여부</th>
                    <th>날씨 ID</th>
                    <th>작성일</th>
                    <th>수정일</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="diary" items="${diaries}">
                    <tr>
                        <td>${diary.id}</td>
                        <td>${diary.userId}</td>
                        <td>${diary.title}</td>
                        <td>${diary.content}</td>
                        <td>
                            <c:choose>
                                <c:when test="${diary.viewScope}">공개</c:when>
                                <c:otherwise>비공개</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${diary.weatherId}</td>
                        <td>${diary.createdAt}</td>
                        <td>${diary.updatedAt}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/diary/delete/${diary.id}" method="post" style="display:inline;">
                                <button type="submit" onclick="return confirm('정말 삭제할까요?')">삭제</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>
        <c:if test="${not empty msg}">
            <script>alert('${msg}');</script>
        </c:if>
    </body>
</html>

