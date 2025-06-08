<%-- 
    Document   : admin_loginPage
    Created on : 2025. 6. 8., 오전 2:13:22
    Author     : Haruki
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>관리자 로그인</title>
    </head>
    <body>
        <h2>관리자 로그인</h2>

    <c:if test="${not empty param.error}">
        <p style="color: red;">로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.</p>
    </c:if>

    <form action="/admin/login" method="post">
        <label>ID: <input type="text" name="id"></label><br><br>
        <label>PW: <input type="password" name="pw"></label><br><br>
        <button type="submit">로그인</button>
    </form>
</body>
</html>

