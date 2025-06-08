<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>주간 일기 통계</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <h2 style="text-align:center;">📊 주간 일기 통계</h2>

        <div style="width: 1280px; height: 720px; margin: auto;">
            <canvas id="diaryChart"></canvas>
        </div>

        <script>
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

            // 통계 데이터 로딩
            window.onload = function () {
                fetch(`${pageContext.request.contextPath}/api/admin/weekly-stats`)
                        .then(res => res.json())
                        .then(data => {
                            const labels = Object.keys(data).sort();
                            const counts = labels.map(date => data[date]);
                            renderChart(labels, counts);
                        })
                        .catch(err => {
                            console.error("❌ fetch 실패:", err);
                            alert("통계를 불러오지 못했습니다.");
                        });
            };
        </script>
    </body>
</html>
