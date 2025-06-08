<%-- 
    Document   : user_list
    Created on : 2025. 6. 7., 오후 8:47:26
    Author     : Haruki
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>사용자 목록</title>
</head>
<body>
    <h2>전체 사용자 목록</h2>
    <table border="1">
        <thead>
            <tr>
                <th>아이디</th>
                <th>닉네임</th>
                <th>권한</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.userid}</td>
                    <td>${user.nickname}</td>
                    <td>${user.role}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>