<%-- 
    Document   : register
    Created on : 2025. 6. 1., 오후 5:29:37
    Author     : Haruki
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>회원가입 동의</title>
    </head>
    <body>
        <h2>회원가입 동의</h2>
        <p>카카오 닉네임: ${sessionScope.temp_nickname}</p>
        <form action="${pageContext.request.contextPath}/api/user/register_confirm" method="post">
            <input type="submit" value="가입에 동의합니다">
        </form>
    </body>
</html>
