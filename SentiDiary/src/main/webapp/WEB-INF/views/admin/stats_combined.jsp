<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>일기 통계</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <h2 style="text-align:center;">📊 일기 통계</h2>

        <!-- 버튼 방식 통계 선택 -->
        <div style="text-align:center; margin-bottom: 20px;">
            <button onclick="loadStats('daily')">일별 (최근 7일)</button>
            <button onclick="loadStats('weekly')">주간별</button>
            <button onclick="loadStats('monthly')">월별</button>
        </div>

        <!-- 차트 영역 -->
        <div style="width: 1280px; height: 720px; margin: auto;">
            <canvas id="diaryChart"></canvas>
        </div>

        <script>
        const contextPath = "<%= request.getContextPath()%>"; // JSP에서 contextPath 안전하게 주입
        let chartInstance = null;

        function renderChart(labels, counts) {
            const ctx = document.getElementById("diaryChart").getContext("2d");
            if (chartInstance) {
                chartInstance.destroy();
            }

            chartInstance = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                            label: '일기 작성 수',
                            data: counts,
                            backgroundColor: 'rgba(54, 162, 235, 0.6)',
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1
                        }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {stepSize: 1}
                        }
                    }
                }
            });
        }

        function loadStats(type) {
            if (!type) {
                console.warn("❌ 통계 타입이 비어 있습니다.");
                return;
            }

            const url = `${contextPath}/api/admin/${type}-stats`;
            console.log("📡 요청 URL:", url);

            fetch(url)
                    .then(res => {
                        if (!res.ok)
                            throw new Error(`HTTP error: ${res.status}`);
                        return res.json();
                    })
                    .then(data => {
                        const labels = Object.keys(data).sort();
                        const counts = labels.map(k => data[k]);
                        renderChart(labels, counts);
                    })
                    .catch(err => {
                        console.error("❌ fetch 실패:", err);
                        alert("통계 데이터를 불러오지 못했습니다.");
                    });
        }

        // 🔥 초기에 버튼 클릭 없이 기본값 로딩
        document.addEventListener("DOMContentLoaded", () => {
            loadStats('daily'); // ✅ 여기서 직접 type 넣기
        });
        </script>
    </body>
</html>
