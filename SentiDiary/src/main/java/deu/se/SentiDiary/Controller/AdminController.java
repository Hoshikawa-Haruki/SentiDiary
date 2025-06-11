/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.Entity.User;
import deu.se.SentiDiary.Repository.UserRepository;
import deu.se.SentiDiary.Service.DiaryService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Haruki
 */
@Slf4j
@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository; // 유저 목록 가져오기 위한 JPA

    @Autowired
    private DiaryService diaryService;

    // 관리자 메인화면 호출
    @GetMapping("/admin_main")
    public String adminMain() {
        return "admin/admin_main";
    }

    // 사용자 리스트 조회
    @GetMapping("/users")
    public String userList(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, 5));
        List<User> userList = userPage.getContent();
        // 사용자별 일기 수 계산
        Map<String, Long> diaryCountMap = diaryService.getDiaryCountPerUser(userList);
        model.addAttribute("userList", userPage.getContent()); // 리스트만 전달
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("diaryCountMap", diaryCountMap); // 추가
        return "admin/user_list";
    }

    // 관리자 - 웹 유저 삭제
    @DeleteMapping("/user/{userid}")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable String userid) {
        try {
            userRepository.deleteById(userid);
            return ResponseEntity.ok("삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }

    // 서버 로그 조회
    @GetMapping("/logs")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getServerLogs() {
        try {
            File logFile = new File("logs/logdata.html");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(logFile));

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=server-log.html")
                    .body(resource);  // OK: InputStreamResource는 Resource 구현체
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 일일 통계 페이지 요청
    @GetMapping("/stats_daily")
    public String statsDaily() {
        return "admin/stats_daily";
    }

    // 주간 통계 페이지 요청
    @GetMapping("/stats_weekly")
    public String statsWeekly() {
        return "admin/stats_weekly";
    }

    // 일기 통계 페이지 요청
    @GetMapping("/stats_combined")
    public String statsPage() {
        return "admin/stats_combined";
    }

    // 7일간 일기 통계 데이터 반환
    @GetMapping("/daily-stats")
    @ResponseBody
    public Map<LocalDate, Long> getDiaryCountWeek() {
        return diaryService.getDiaryCountsLast7Days();
    }

    // 주간 일기 통계 데이터 반환
    @GetMapping("/weekly-stats")
    @ResponseBody
    public Map<String, Long> getWeeklyDiaryStats() {
        return diaryService.getDiaryCountsByWeek();
    }

    // 월간 일기 통계 데이터 반환
    @GetMapping("/monthly-stats")
    @ResponseBody
    public Map<String, Long> getMonthlyDiaryStats() {
        return diaryService.getDiaryCountsByMonth(); // 예: {"2025-03": 10, "2025-04": 12}
    }

}
