/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Controller;

import deu.se.SentiDiary.Model.Diary;
import deu.se.SentiDiary.Service.DiaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Haruki
 */
@Controller
@Slf4j
public class DiaryViewController {

    @Autowired
    DiaryService diaryService;

    @GetMapping("/test/form") // 지피티가 넣어준거. 굳이?
    public String showForm(Model model) {
        model.addAttribute("diary", new Diary());
        return "form";
    }

    @PostMapping("/form/createDiary")
    public String createDiaryForm(@ModelAttribute Diary diary) {
        diaryService.createDiary(diary);
        return "redirect:/"; // 작성 후 인덱스로 복귀
    }

    @GetMapping("/diary/list")
    public String showDiaryList(Model model) {
        model.addAttribute("diaries", diaryService.getAllDiaries());
        return "diary_list";  // → /WEB-INF/views/diary_list.jsp
    }
}
