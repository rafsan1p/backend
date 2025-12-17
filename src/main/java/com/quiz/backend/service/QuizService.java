package com.quiz.backend.service;

import com.quiz.backend.model.Question;
import com.quiz.backend.model.QuizResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class QuizService {
    
    // In-Memory Storage
    private final Map<Long, Question> questions = new HashMap<>();
    private final List<QuizResult> results = new ArrayList<>();
    private final AtomicLong questionIdCounter = new AtomicLong(1);
    private final AtomicLong resultIdCounter = new AtomicLong(1);
    
    // Initialize with sample questions
    public QuizService() {
        initializeSampleQuestions();
    }
    
    // Question Operations
    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions.values());
    }
    
    public List<Question> getQuestionsByCategory(String category) {
        List<Question> categoryQuestions = questions.values().stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        Collections.shuffle(categoryQuestions);
        return categoryQuestions;
    }
    
    public List<Question> getQuestionsByCategoryAndDifficulty(String category, String difficulty) {
        List<Question> filtered = questions.values().stream()
                .filter(q -> q.getCategory().equalsIgnoreCase(category) && 
                           q.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());
        Collections.shuffle(filtered);
        return filtered;
    }
    
    public List<String> getAllCategories() {
        return questions.values().stream()
                .map(Question::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public Question addQuestion(Question question) {
        question.setId(questionIdCounter.getAndIncrement());
        questions.put(question.getId(), question);
        return question;
    }
    
    public Optional<Question> getQuestionById(Long id) {
        return Optional.ofNullable(questions.get(id));
    }
    
    public void deleteQuestion(Long id) {
        questions.remove(id);
    }
    
    // Score Calculation
    public int calculateScore(List<Integer> userAnswers, List<Long> questionIds) {
        int score = 0;
        for (int i = 0; i < questionIds.size(); i++) {
            Question question = questions.get(questionIds.get(i));
            if (question != null && question.getCorrectAnswer() == userAnswers.get(i)) {
                score++;
            }
        }
        return score;
    }
    
    // Quiz Result Operations
    public QuizResult saveResult(QuizResult result) {
        result.setId(resultIdCounter.getAndIncrement());
        results.add(result);
        return result;
    }
    
    public List<QuizResult> getLeaderboard() {
        return results.stream()
                .sorted(Comparator.comparingInt(QuizResult::getScore).reversed()
                        .thenComparing(QuizResult::getCompletedAt).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
    
    public List<QuizResult> getLeaderboardByCategory(String category) {
        return results.stream()
                .filter(r -> r.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparingInt(QuizResult::getScore).reversed()
                        .thenComparing(QuizResult::getCompletedAt).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
    
    public List<QuizResult> getUserResults(String userEmail) {
        return results.stream()
                .filter(r -> r.getUserEmail().equalsIgnoreCase(userEmail))
                .sorted(Comparator.comparing(QuizResult::getCompletedAt).reversed())
                .collect(Collectors.toList());
    }
    
    public List<QuizResult> getAllResults() {
        return new ArrayList<>(results);
    }
    
    // Initialize Sample Questions
    private void initializeSampleQuestions() {
        // Programming - Easy
        addQuestion(new Question(null, "What does HTML stand for?",
                Arrays.asList("Hyper Text Markup Language", "High Tech Modern Language", 
                            "Home Tool Markup Language", "Hyperlinks and Text Markup Language"),
                0, "Programming", "Easy"));
        
        addQuestion(new Question(null, "Which language is primarily used for web apps?",
                Arrays.asList("PHP", "Python", "JavaScript", "All of the above"),
                3, "Programming", "Easy"));
        
        addQuestion(new Question(null, "CSS stands for?",
                Arrays.asList("Cascading Style Sheets", "Computer Style Sheets", 
                            "Creative Style Sheets", "Colorful Style Sheets"),
                0, "Programming", "Easy"));
        
        addQuestion(new Question(null, "What is the correct syntax for referring to an external script?",
                Arrays.asList("<script src='file.js'>", "<script href='file.js'>", 
                            "<script name='file.js'>", "<script link='file.js'>"),
                0, "Programming", "Easy"));
        
        addQuestion(new Question(null, "Inside which HTML element do we put JavaScript?",
                Arrays.asList("<js>", "<scripting>", "<script>", "<javascript>"),
                2, "Programming", "Easy"));
        
        // Programming - Medium
        addQuestion(new Question(null, "Which of the following is NOT a JavaScript data type?",
                Arrays.asList("Undefined", "Number", "Boolean", "Float"),
                3, "Programming", "Medium"));
        
        addQuestion(new Question(null, "What is the correct way to create a function in JavaScript?",
                Arrays.asList("function myFunction()", "function:myFunction()", 
                            "function = myFunction()", "create myFunction()"),
                0, "Programming", "Medium"));
        
        addQuestion(new Question(null, "How do you declare a variable in JavaScript?",
                Arrays.asList("var myVar;", "variable myVar;", "v myVar;", "dim myVar;"),
                0, "Programming", "Medium"));
        
        addQuestion(new Question(null, "Which operator is used to assign a value to a variable?",
                Arrays.asList("*", "=", "-", "x"),
                1, "Programming", "Medium"));
        
        addQuestion(new Question(null, "What will 'typeof null' return?",
                Arrays.asList("null", "undefined", "object", "number"),
                2, "Programming", "Medium"));
        
        // Programming - Hard
        addQuestion(new Question(null, "What is a closure in JavaScript?",
                Arrays.asList("A function with no return value", 
                            "A function that has access to outer function's variables",
                            "A function that cannot be called", 
                            "A function without parameters"),
                1, "Programming", "Hard"));
        
        addQuestion(new Question(null, "What is the time complexity of binary search?",
                Arrays.asList("O(n)", "O(log n)", "O(n^2)", "O(1)"),
                1, "Programming", "Hard"));
        
        addQuestion(new Question(null, "Which design pattern ensures a class has only one instance?",
                Arrays.asList("Factory", "Singleton", "Observer", "Decorator"),
                1, "Programming", "Hard"));
        
        // Science - Easy
        addQuestion(new Question(null, "What planet is known as the Red Planet?",
                Arrays.asList("Venus", "Mars", "Jupiter", "Saturn"),
                1, "Science", "Easy"));
        
        addQuestion(new Question(null, "What is the chemical symbol for water?",
                Arrays.asList("H2O", "CO2", "O2", "NaCl"),
                0, "Science", "Easy"));
        
        addQuestion(new Question(null, "How many bones are in the adult human body?",
                Arrays.asList("196", "206", "216", "226"),
                1, "Science", "Easy"));
        
        addQuestion(new Question(null, "What is the center of an atom called?",
                Arrays.asList("Electron", "Proton", "Nucleus", "Neutron"),
                2, "Science", "Easy"));
        
        addQuestion(new Question(null, "What gas do plants absorb from the atmosphere?",
                Arrays.asList("Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"),
                2, "Science", "Easy"));
        
        // Science - Medium
        addQuestion(new Question(null, "What is the powerhouse of the cell?",
                Arrays.asList("Nucleus", "Ribosome", "Mitochondria", "Endoplasmic Reticulum"),
                2, "Science", "Medium"));
        
        addQuestion(new Question(null, "What is the speed of light in vacuum?",
                Arrays.asList("299,792,458 m/s", "150,000,000 m/s", 
                            "350,000,000 m/s", "299,792 m/s"),
                0, "Science", "Medium"));
        
        addQuestion(new Question(null, "What is the pH of pure water?",
                Arrays.asList("5", "6", "7", "8"),
                2, "Science", "Medium"));
        
        // Science - Hard
        addQuestion(new Question(null, "What is Heisenberg's Uncertainty Principle about?",
                Arrays.asList("Position and momentum cannot both be precisely known",
                            "Energy cannot be created or destroyed",
                            "Every action has an equal reaction",
                            "Matter cannot be created or destroyed"),
                0, "Science", "Hard"));
        
        addQuestion(new Question(null, "What particle is exchanged in electromagnetic interactions?",
                Arrays.asList("Gluon", "W Boson", "Photon", "Higgs Boson"),
                2, "Science", "Hard"));
        
        // Mathematics - Easy
        addQuestion(new Question(null, "What is 7 × 8?",
                Arrays.asList("54", "56", "64", "72"),
                1, "Mathematics", "Easy"));
        
        addQuestion(new Question(null, "What is the value of π (pi) approximately?",
                Arrays.asList("2.14", "3.14", "4.14", "5.14"),
                1, "Mathematics", "Easy"));
        
        addQuestion(new Question(null, "What is 15% of 100?",
                Arrays.asList("10", "15", "20", "25"),
                1, "Mathematics", "Easy"));
        
        addQuestion(new Question(null, "What is the sum of angles in a triangle?",
                Arrays.asList("90°", "180°", "270°", "360°"),
                1, "Mathematics", "Easy"));
        
        // Mathematics - Medium
        addQuestion(new Question(null, "What is the square root of 144?",
                Arrays.asList("10", "11", "12", "13"),
                2, "Mathematics", "Medium"));
        
        addQuestion(new Question(null, "What is the derivative of x²?",
                Arrays.asList("x", "2x", "x²", "2"),
                1, "Mathematics", "Medium"));
        
        addQuestion(new Question(null, "What is the formula for the area of a circle?",
                Arrays.asList("πr", "πr²", "2πr", "πd"),
                1, "Mathematics", "Medium"));
        
        // Mathematics - Hard
        addQuestion(new Question(null, "What is Euler's number (e) approximately?",
                Arrays.asList("2.178", "2.518", "2.718", "3.142"),
                2, "Mathematics", "Hard"));
        
        addQuestion(new Question(null, "What is the integral of 1/x?",
                Arrays.asList("ln(x)", "x²", "e^x", "1/x²"),
                0, "Mathematics", "Hard"));
        
        // History - Easy
        addQuestion(new Question(null, "Who was the first President of the United States?",
                Arrays.asList("Thomas Jefferson", "George Washington", 
                            "Abraham Lincoln", "John Adams"),
                1, "History", "Easy"));
        
        addQuestion(new Question(null, "In which year did World War II end?",
                Arrays.asList("1943", "1944", "1945", "1946"),
                2, "History", "Easy"));
        
        addQuestion(new Question(null, "Who discovered America in 1492?",
                Arrays.asList("Vasco da Gama", "Christopher Columbus", 
                            "Ferdinand Magellan", "Marco Polo"),
                1, "History", "Easy"));
        
        // History - Medium
        addQuestion(new Question(null, "Who wrote the Declaration of Independence?",
                Arrays.asList("George Washington", "Benjamin Franklin", 
                            "Thomas Jefferson", "John Adams"),
                2, "History", "Medium"));
        
        addQuestion(new Question(null, "In which year did the Berlin Wall fall?",
                Arrays.asList("1987", "1988", "1989", "1990"),
                2, "History", "Medium"));
        
        // History - Hard
        addQuestion(new Question(null, "What was the capital of the Byzantine Empire?",
                Arrays.asList("Rome", "Athens", "Constantinople", "Alexandria"),
                2, "History", "Hard"));
        
        // Geography - Easy
        addQuestion(new Question(null, "What is the capital of France?",
                Arrays.asList("London", "Berlin", "Paris", "Madrid"),
                2, "Geography", "Easy"));
        
        addQuestion(new Question(null, "Which is the largest ocean?",
                Arrays.asList("Atlantic", "Indian", "Arctic", "Pacific"),
                3, "Geography", "Easy"));
        
        addQuestion(new Question(null, "How many continents are there?",
                Arrays.asList("5", "6", "7", "8"),
                2, "Geography", "Easy"));
        
        // Geography - Medium
        addQuestion(new Question(null, "What is the longest river in the world?",
                Arrays.asList("Amazon", "Nile", "Yangtze", "Mississippi"),
                1, "Geography", "Medium"));
        
        addQuestion(new Question(null, "Which country has the most time zones?",
                Arrays.asList("Russia", "USA", "China", "France"),
                3, "Geography", "Medium"));
        
        // Geography - Hard
        addQuestion(new Question(null, "What is the smallest country in the world?",
                Arrays.asList("Monaco", "Vatican City", "San Marino", "Liechtenstein"),
                1, "Geography", "Hard"));
        
        System.out.println("✅ " + questions.size() + " sample questions loaded successfully!");
    }
}