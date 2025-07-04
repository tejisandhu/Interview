package com.interview.quiz.Controller;

import com.interview.quiz.Entity.InterviewRoom;

import com.interview.quiz.Entity.Languages;

import com.interview.quiz.Entity.Question;
import com.interview.quiz.Entity.Quiz;
import com.interview.quiz.Entity.QuizHistory;
import com.interview.quiz.Entity.User;
import com.interview.quiz.Repository.InterviewRoomRepository;
import com.interview.quiz.Repository.LanguagesRepository;
import com.interview.quiz.Repository.QuizHistoryRepository;
import com.interview.quiz.Repository.QuizRepository;
import com.interview.quiz.Repository.UserRepository;
import com.interview.quiz.Service.EmailService;
import com.interview.quiz.Service.UserService;
import com.interview.quiz.Entity.InterviewRoom;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.security.Principal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private LanguagesRepository languagesRepository;

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizHistoryRepository quizHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InterviewRoomRepository roomRepo;
    @Autowired
    private EmailService emailService;



    


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
    @GetMapping("/user/user-dashboard")
    public String showLanguages(Model model) {
        List<Languages> languages = languagesRepository.findAll();
        model.addAttribute("languages", languages);
        return "user-dashboard"; // Template name
    }

    @GetMapping("/user/quizzes")
    public String showQuizzesByLanguage(@RequestParam("languageId") Long languageId, Model model) {
        List<Quiz> quizzes = quizRepository.findByLanguageId(languageId);
        Languages language = languagesRepository.findById(languageId).orElse(null);
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("language", language);
        return "quizzes-by-language"; // Template name
    }
    @GetMapping("/user/start-test")
    public String startTest(@RequestParam("quizId") Long quizId, Model model) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<Question> questions = quiz.getQuestions(); // Or fetch via service

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        return "start-test";  // Thymeleaf template
    }
//    @PostMapping("/user/submit-test")
//    public String submitTest(@RequestParam("quizId") Long quizId,
//                             @RequestParam Map<String, String> allParams,
//                             Model model,
//                             Principal principal) {
//        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
//        List<Question> questions = quiz.getQuestions();
//
//        int total = questions.size();
//        int correct = 0;
//
//        for (Question q : questions) {
//            String submittedAnswer = allParams.get("answers[" + q.getId() + "]");
//            if (q.getAnswer().equalsIgnoreCase(submittedAnswer)) {
//                correct++;
//            }
//        }
//        User user = userService.getUserByEmail(principal.getName());
//
//        // Save quiz history
//        QuizHistory history = new QuizHistory();
//        history.setQuiz(quiz);
//        history.setUser(user);
//        history.setScore(correct);
//        history.setTotalQuestions(total);
//        history.setSubmittedAt(LocalDateTime.now());
//        quizHistoryRepository.save(history);
//
//
//        model.addAttribute("score", correct);
//        model.addAttribute("total", total);
//        return "test-result";  // new template to display result
//    }
    @PostMapping("/user/submit-test")
    public String submitTest(@RequestParam("quizId") Long quizId,
                             @RequestParam Map<String, String> allParams,
                             Model model,
                             Principal principal) {

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<Question> questions = quiz.getQuestions();

        int total = questions.size();
        int correct = 0;

        for (Question q : questions) {
            String submittedAnswer = allParams.get("answers[" + q.getId() + "]");
            if (q.getAnswer().equalsIgnoreCase(submittedAnswer)) {
                correct++;
            }
        }

        // ✅ Get user directly from repository
        User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

        // Save quiz history
        QuizHistory history = new QuizHistory();
        history.setQuiz(quiz);
        history.setUser(user);
        history.setScore(correct);
        history.setStartTime(LocalDateTime.now());
        history.setEndTime(LocalDateTime.now());

        quizHistoryRepository.save(history);

        model.addAttribute("score", correct);
        model.addAttribute("total", total);
        return "test-result";
    }
    @GetMapping("/user/history")
    public String showQuizHistory(Model model, Principal principal) {
        String email = principal.getName();

        // Get user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get history records
        List<QuizHistory> historyList = quizHistoryRepository.findByUser(user);
        model.addAttribute("historyList", historyList);

        return "user-quiz-history"; // Create this Thymeleaf template
    }
    @PostMapping("/user/create-room")
    public String createRoom(@RequestParam("candidateEmail") String candidateEmail,
                             Principal principal, Model model) {
        // Either use by email (if principal name is email)
        User recruiter = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Or use this if principal returns ID:
        // Long userId = Long.parseLong(principal.getName());
        // User recruiter = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        InterviewRoom room = new InterviewRoom();
        room.setCreatedByUserId(recruiter.getId());
        roomRepo.save(room);

        String roomLink = "http://localhost:8080/user/join-room/" + room.getRoomId();
        emailService.sendInvite(candidateEmail, recruiter.getName(), roomLink);

        model.addAttribute("roomId", room.getRoomId());
        return "room-created";
    }
    @GetMapping("/user/join-room/{roomId}")
    public String joinRoom(@PathVariable String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "interview"; // interview.html
    }







}
