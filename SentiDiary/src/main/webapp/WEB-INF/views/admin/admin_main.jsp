<%-- 
    Document   : admin_main
    Created on : 2025. 6. 8., 오전 2:12:16
    Author     : Haruki
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userid = (String) session.getAttribute("userid");
    String role = (String) session.getAttribute("role");
%>
<html>
    <head><title>관리자 메뉴</title></head>
    <body>
        <h2>관리자 메뉴</h2>
        <p>안녕하세요, <%= userid%> (권한: <%= role%>) 님!</p>

        <ul>
            <li><a href="<%= request.getContextPath()%>/api/admin/users">사용자 목록</a></li>
            <li><a href="<%= request.getContextPath()%>/api/admin/stats_combined"> 통계 보기</a></li>
            <li><a href="<%= request.getContextPath()%>/api/admin/stats_daily"> 일간 통계</a></li>
            <li><a href="<%= request.getContextPath()%>/api/admin/stats_weekly"> 주간 통계</a></li>
            <li><a href="<%= request.getContextPath()%>/api/admin/logs" target="_blank">서버 로그 보기</a></li>
            <li><a href="<%= request.getContextPath()%>/api/logout">로그아웃</a></li>
        </ul>
    </body>
</html>
