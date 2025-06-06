<%-- 
    Document   : kakaologin
    Created on : 2025. 6. 1., 오전 3:24:10
    Author     : Haruki
    
06.07 플러터에서 직접 카카오 인증 주소 호출 방식으로 변경
해당 페이지 사용 X
--%>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>카카오 로그인</title>
        <style>
            * {
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #FEE500;
                margin: 0;
                padding: 0;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
            }

            .login-container {
                background: #ffffff;
                padding: 32px;
                border-radius: 16px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                text-align: center;
                width: 90%;
                max-width: 360px;
            }

            .login-container h1 {
                font-size: 22px;
                margin-bottom: 24px;
                color: #3C1E1E;
            }

            .kakao-login-btn {
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: #FEE500;
                border: none;
                border-radius: 8px;
                padding: 14px;
                font-size: 16px;
                font-weight: bold;
                color: #3C1E1E;
                text-decoration: none;
                cursor: pointer;
                transition: background-color 0.3s ease;
                width: 100%;
            }

            .kakao-login-btn:hover {
                background-color: #f4d700;
            }

            .kakao-login-btn img {
                width: 24px;
                height: 24px;
                margin-right: 10px;
            }

            @media (max-height: 500px) {
                .login-container {
                    margin-top: 40px;
                    padding: 24px;
                }
            }

            @media (max-width: 320px) {
                .login-container h1 {
                    font-size: 18px;
                }

                .kakao-login-btn {
                    padding: 12px;
                    font-size: 14px;
                }
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <h1>카카오 로그인</h1>
            <!--    <a href="/SentiDiary/kakao/login">-->
            <a class="kakao-login-btn" href="https://joint-cheetah-helpful.ngrok-free.app/SentiDiary/kakao/login">
                <img src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_small.png" alt="Kakao Icon">
                카카오로 시작하기
            </a>
        </div>
    </body>
</html>
