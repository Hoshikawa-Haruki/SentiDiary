<%-- 
    Document   : loginSuccess
    Created on : 2025. 6. 1., 오후 6:16:02
    Author     : Haruki
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>로그인 성공</title>
    </head>
    <body>
        <h1>환영합니다, ${sessionScope.nickname}님!</h1>
        <form action="${pageContext.request.contextPath}/" method="get">
            <button type="submit">메인 화면으로</button>
        </form>
    </body>
</html>
