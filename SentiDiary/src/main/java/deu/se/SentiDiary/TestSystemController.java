/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary;

import deu.se.SentiDiary.Service.DiaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Haruki
 */
@Controller
@Slf4j
public class TestSystemController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping("/test/createDiary")
    public String createDiary() {
        log.info("● TestCreateDiary is called...");
        return "createDiary";
    }

    @GetMapping("/test/showDiary")
    public String showDiary(Model model) {
        model.addAttribute("diaries", diaryService.getAllDiaries());
        log.info("● TestShowDiary is called...");
        return "showDiaryList";  // → /WEB-INF/views/showDiaryList.jsp
    }

    @PostMapping("/diary/delete/{id}")
    public String deleteDiary(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        diaryService.deleteDiary(id);
        redirectAttributes.addFlashAttribute("msg", "성공적으로 삭제되었습니다.");
        return "redirect:/test/showDiary";
    }
}
