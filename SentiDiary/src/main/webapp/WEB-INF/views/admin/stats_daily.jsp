<%-- 
    Document   : stats_week
    Created on : 2025. 6. 9., 오전 3:25:53
    Author     : Haruki
--%>

<%@page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>일기 통계</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <h2>최근 7일간 일기 작성 수</h2>
        <div style="width: 1280px; height: 720px; margin: auto;">
            <canvas id="diaryChart"></canvas>
        </div>


        <script>
            let chartInstance = null;

            function renderChart(labels, counts) {
                const ctx = document.getElementById('diaryChart').getContext('2d');

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
                fetch(`${pageContext.request.contextPath}/api/admin/daily-stats`)
                        .then(res => res.json())
                        .then(data => {
                            const labels = Object.keys(data).sort();
                            const counts = labels.map(label => data[label]);
                            renderChart(labels, counts);
                        })
                        .catch(err => {
                            console.error("❌ fetch error:", err);
                        });
            }

            // 초기 로딩
            window.onload = function () {
                loadStats('daily'); // default

                document.getElementById('statType').addEventListener('change', function () {
                    const type = this.value;
                    loadStats(type);
                });
            };
        </script>
    </body>
</html>




