package com.quiz.backend.controller;

import com.quiz.backend.model.Question;
import com.quiz.backend.model.QuizResult;
import com.quiz.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "http://localhost:5173")
public class QuizController {
    
    @Autowired
    private QuizService quizService;
    
    @GetMapping("/")
    public String welcome() {
        return "🎯 Quiz Application API is running! Total questions: " + quizService.getAllQuestions().size();
    }
    
    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(quizService.getAllQuestions());
    }
    
    @GetMapping("/questions/category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(quizService.getQuestionsByCategory(category));
    }
    
    @GetMapping("/questions/category/{category}/difficulty/{difficulty}")
    public ResponseEntity<List<Question>> getQuestionsByCategoryAndDifficulty(
            @PathVariable String category,
            @PathVariable String difficulty) {
        return ResponseEntity.ok(quizService.getQuestionsByCategoryAndDifficulty(category, difficulty));
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(quizService.getAllCategories());
    }
    
    @PostMapping("/questions")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.addQuestion(question));
    }
    
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        quizService.deleteQuestion(id);
        return ResponseEntity.ok("Question deleted successfully");
    }
    
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitQuiz(@RequestBody Map<String, Object> submission) {
        try {
            String userName = (String) submission.get("userName");
            String userEmail = (String) submission.get("userEmail");
            String category = (String) submission.get("category");
            String difficulty = (String) submission.get("difficulty");
            Integer timeTaken = (Integer) submission.get("timeTaken");
            
            @SuppressWarnings("unchecked")
            List<Integer> userAnswers = (List<Integer>) submission.get("userAnswers");
            
            @SuppressWarnings("unchecked")
            List<Integer> questionIdsInt = (List<Integer>) submission.get("questionIds");
            List<Long> questionIds = questionIdsInt.stream().map(Long::valueOf).toList();
            
            int score = quizService.calculateScore(userAnswers, questionIds);
            int total = questionIds.size();
            double percentage = (score * 100.0) / total;
            
            QuizResult result = new QuizResult(null, userName, userEmail, score, total, 
                                              category, difficulty, timeTaken);
            quizService.saveResult(result);
            
            Map<String, Object> response = new HashMap<>();
            response.put("score", score);
            response.put("total", total);
            response.put("percentage", percentage);
            response.put("message", getScoreMessage(percentage));
            response.put("passed", percentage >= 50);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to submit quiz: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @GetMapping("/leaderboard")
    public ResponseEntity<List<QuizResult>> getLeaderboard() {
        return ResponseEntity.ok(quizService.getLeaderboard());
    }
    
    @GetMapping("/leaderboard/category/{category}")
    public ResponseEntity<List<QuizResult>> getLeaderboardByCategory(@PathVariable String category) {
        return ResponseEntity.ok(quizService.getLeaderboardByCategory(category));
    }
    
    @GetMapping("/results/user/{userEmail}")
    public ResponseEntity<List<QuizResult>> getUserResults(@PathVariable String userEmail) {
        return ResponseEntity.ok(quizService.getUserResults(userEmail));
    }
    
    @GetMapping("/results")
    public ResponseEntity<List<QuizResult>> getAllResults() {
        return ResponseEntity.ok(quizService.getAllResults());
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQuestions", quizService.getAllQuestions().size());
        stats.put("totalCategories", quizService.getAllCategories().size());
        stats.put("categories", quizService.getAllCategories());
        stats.put("totalAttempts", quizService.getAllResults().size());
        return ResponseEntity.ok(stats);
    }
    
    private String getScoreMessage(double percentage) {
        if (percentage >= 90) return "Outstanding! 🎉";
        else if (percentage >= 70) return "Great job! 👏";
        else if (percentage >= 50) return "Good effort! 👍";
        else return "Keep practicing! 📚";
    }
}