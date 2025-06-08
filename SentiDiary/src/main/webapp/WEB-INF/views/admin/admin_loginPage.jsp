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
        <form action="${pageContext.request.contextPath}/login.do" method="post">
            아이디: <input type="text" name="userid" value="${loginErrorUserid != null ? loginErrorUserid : ''}" /><br/>
            비밀번호: <input type="password" name="passwd" /><br/>
            <input type="submit" value="로그인" />
        </form>
    <c:if test="${not empty loginErrorUserid}">
        <p style="color:red;">로그인 실패: 아이디 혹은 비밀번호 확인</p>
    </c:if>
</body>
</html>

