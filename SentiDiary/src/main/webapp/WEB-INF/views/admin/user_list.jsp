<%-- 
    Document   : user_list
    Created on : 2025. 6. 7., 오후 8:47:26
    Author     : Haruki
--%>

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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/admin_style.css">
        <script>
            function deleteUser(userid) {
                if (confirm(userid + " 사용자를 삭제하시겠습니까?")) {
                    fetch('/SentiDiary/admin/user/' + userid, {
                        method: 'DELETE'
                    }).then(res => {
                        if (res.ok) {
                            alert("삭제 완료!");
                            location.reload();
                        } else {
                            alert("삭제 실패");
                        }
                    });
                }
            }
        </script>
    </head>
    <body>
        <h2>전체 사용자 목록</h2>

        <table border="1">
            <thead>
                <tr>
                    <th>아이디</th>
                    <th>닉네임</th>
                    <th>권한</th>
                    <th>작성한 일기 수</th>
                    <th>삭제</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.userid}</td>
                        <td>${user.nickname}</td>
                        <td>${user.role}</td>
                        <td>${diaryCountMap[user.userid]}</td>
                        <td><button onclick="deleteUser('${user.userid}')">삭제</button></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- 페이지네이션 -->
        <c:if test="${totalPages > 1}">
            <div>
                <c:forEach begin="0" end="${totalPages - 1}" var="i">
                    <a href="?page=${i}">${i + 1}</a>
                </c:forEach>
            </div>
        </c:if>
    </body>
</html>
