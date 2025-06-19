package com.interview.quiz.Controller;

import com.interview.quiz.Entity.Languages;
import com.interview.quiz.Entity.Quiz;
import com.interview.quiz.Service.LanguageService;
import com.interview.quiz.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("")
public class AdminQuizController {

    @Autowired
    private LanguageService languageService;

    @Autowired
    private QuizService quizService;
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
    	System.out.println("GOT THE REQUEST00");
        model.addAttribute("language", new Languages());
        model.addAttribute("quiz", new Quiz());
        model.addAttribute("languages", languageService.getAllLanguages()); // for quiz dropdown
        return "dashboard";
    }


    @GetMapping("/admin/add-language")
    public String showAddLanguageForm(Model model) {
        model.addAttribute("language", new Languages());
        return "add-language";
    }

    @PostMapping("/admin/add-language")
    public String addLanguage(@ModelAttribute Languages language) {
        languageService.addLanguage(language.getName());
        return "redirect:/admin/add-language?success";
    }

    @GetMapping("/admin/add-quiz")
    public String showAddQuizForm(Model model) {
        model.addAttribute("quiz", new Quiz());
        model.addAttribute("languages", languageService.getAllLanguages());
        return "add-quiz";
    }

    @PostMapping("/admin/add-quiz")
    public String addQuiz(@ModelAttribute Quiz quiz, @RequestParam Long languageId) {
        quizService.addQuiz(quiz.getName(), quiz.getLevel(), languageId);
        return "redirect:/admin/add-quiz?success";
    }
}
