package com.interview.quiz.Controller;

import com.interview.quiz.Entity.User;
import com.interview.quiz.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show Sign Up form
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());  // "user" must match th:object
        return "signup";
    }

    // Handle sign-up form submission
    @PostMapping("/signup")
    public String processSignUp(@ModelAttribute("user") User user, Model model) {
        try {
            userService.addNew(user);
            return "redirect:/login?signupSuccess"; // assuming /login exists
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }
    
}
