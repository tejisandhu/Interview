package com.interview.quiz.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class InterviewController {

    @GetMapping("/interview/room/{roomId}")
    public String interviewRoom(@PathVariable String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "interview"; // Thymeleaf template
    }
}