package com.quiz.backend.model;

import java.time.LocalDateTime;

public class QuizResult {
    private Long id;
    private String userName;
    private String userEmail;
    private int score;
    private int totalQuestions;
    private String category;
    private String difficulty;
    private LocalDateTime completedAt;
    private int timeTaken;
    
    public QuizResult() {
        this.completedAt = LocalDateTime.now();
    }
    
    public QuizResult(Long id, String userName, String userEmail, int score, int totalQuestions, 
                      String category, String difficulty, int timeTaken) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.category = category;
        this.difficulty = difficulty;
        this.timeTaken = timeTaken;
        this.completedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public int getTimeTaken() {
        return timeTaken;
    }
    
    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }
    
    public double getPercentage() {
        return (score * 100.0) / totalQuestions;
    }
}