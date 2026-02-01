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
        // Programming - Easy (5 questions)
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
        
        // Programming - Medium (5 questions)
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
        
        // Programming - Hard (5 questions)
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
        
        addQuestion(new Question(null, "What does 'hoisting' mean in JavaScript?",
                Arrays.asList("Moving variables to the top of their scope", 
                            "Lifting heavy objects", 
                            "Creating new functions", 
                            "Deleting variables"),
                0, "Programming", "Hard"));
        
        addQuestion(new Question(null, "What is the difference between '==' and '===' in JavaScript?",
                Arrays.asList("No difference", 
                            "=== checks type and value, == only checks value",
                            "== is faster than ===", 
                            "=== is deprecated"),
                1, "Programming", "Hard"));
        
        // Science - Easy (5 questions)
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
        
        // Science - Medium (5 questions)
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
        
        addQuestion(new Question(null, "What is the most abundant gas in Earth's atmosphere?",
                Arrays.asList("Oxygen", "Carbon Dioxide", "Nitrogen", "Argon"),
                2, "Science", "Medium"));
        
        addQuestion(new Question(null, "What type of bond holds water molecules together?",
                Arrays.asList("Ionic", "Covalent", "Hydrogen", "Metallic"),
                2, "Science", "Medium"));
        
        // Science - Hard (5 questions)
        addQuestion(new Question(null, "What is Heisenberg's Uncertainty Principle about?",
                Arrays.asList("Position and momentum cannot both be precisely known",
                            "Energy cannot be created or destroyed",
                            "Every action has an equal reaction",
                            "Matter cannot be created or destroyed"),
                0, "Science", "Hard"));
        
        addQuestion(new Question(null, "What particle is exchanged in electromagnetic interactions?",
                Arrays.asList("Gluon", "W Boson", "Photon", "Higgs Boson"),
                2, "Science", "Hard"));
        
        addQuestion(new Question(null, "What is the half-life of Carbon-14?",
                Arrays.asList("5,730 years", "1,000 years", "10,000 years", "100,000 years"),
                0, "Science", "Hard"));
        
        addQuestion(new Question(null, "What is the name of the theoretical boundary around a black hole?",
                Arrays.asList("Photon Sphere", "Event Horizon", "Singularity", "Accretion Disk"),
                1, "Science", "Hard"));
        
        addQuestion(new Question(null, "What is the strongest force in nature?",
                Arrays.asList("Electromagnetic", "Weak Nuclear", "Strong Nuclear", "Gravitational"),
                2, "Science", "Hard"));
        
        // Mathematics - Easy (5 questions)
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
        
        addQuestion(new Question(null, "What is 12 + 8?",
                Arrays.asList("18", "19", "20", "21"),
                2, "Mathematics", "Easy"));
        
        // Mathematics - Medium (5 questions)
        addQuestion(new Question(null, "What is the square root of 144?",
                Arrays.asList("10", "11", "12", "13"),
                2, "Mathematics", "Medium"));
        
        addQuestion(new Question(null, "What is the derivative of x²?",
                Arrays.asList("x", "2x", "x²", "2"),
                1, "Mathematics", "Medium"));
        
        addQuestion(new Question(null, "What is the formula for the area of a circle?",
                Arrays.asList("πr", "πr²", "2πr", "πd"),
                1, "Mathematics", "Medium"));
        
        addQuestion(new Question(null, "What is the slope of the line y = 3x + 2?",
                Arrays.asList("2", "3", "5", "1"),
                1, "Mathematics", "Medium"));
        
        addQuestion(new Question(null, "What is 2³?",
                Arrays.asList("6", "8", "9", "12"),
                1, "Mathematics", "Medium"));
        
        // Mathematics - Hard (5 questions)
        addQuestion(new Question(null, "What is Euler's number (e) approximately?",
                Arrays.asList("2.178", "2.518", "2.718", "3.142"),
                2, "Mathematics", "Hard"));
        
        addQuestion(new Question(null, "What is the integral of 1/x?",
                Arrays.asList("ln(x)", "x²", "e^x", "1/x²"),
                0, "Mathematics", "Hard"));
        
        addQuestion(new Question(null, "What is the limit of (sin x)/x as x approaches 0?",
                Arrays.asList("0", "1", "∞", "undefined"),
                1, "Mathematics", "Hard"));
        
        addQuestion(new Question(null, "What is the determinant of a 2x2 matrix [[a,b],[c,d]]?",
                Arrays.asList("ad + bc", "ad - bc", "ac - bd", "ab - cd"),
                1, "Mathematics", "Hard"));
        
        addQuestion(new Question(null, "What is the Taylor series expansion of e^x around x=0?",
                Arrays.asList("1 + x + x²/2! + x³/3! + ...", "x + x²/2 + x³/3 + ...", 
                            "1 + x + x² + x³ + ...", "x + x² + x³ + ..."),
                0, "Mathematics", "Hard"));
        
        // History - Easy (5 questions)
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
        
        addQuestion(new Question(null, "Which ancient wonder of the world was located in Egypt?",
                Arrays.asList("Hanging Gardens", "Colossus of Rhodes", 
                            "Great Pyramid of Giza", "Lighthouse of Alexandria"),
                2, "History", "Easy"));
        
        addQuestion(new Question(null, "In which year did the Titanic sink?",
                Arrays.asList("1910", "1911", "1912", "1913"),
                2, "History", "Easy"));
        
        // History - Medium (5 questions)
        addQuestion(new Question(null, "Who wrote the Declaration of Independence?",
                Arrays.asList("George Washington", "Benjamin Franklin", 
                            "Thomas Jefferson", "John Adams"),
                2, "History", "Medium"));
        
        addQuestion(new Question(null, "In which year did the Berlin Wall fall?",
                Arrays.asList("1987", "1988", "1989", "1990"),
                2, "History", "Medium"));
        
        addQuestion(new Question(null, "Which empire was ruled by Julius Caesar?",
                Arrays.asList("Greek Empire", "Roman Empire", "Persian Empire", "Egyptian Empire"),
                1, "History", "Medium"));
        
        addQuestion(new Question(null, "What was the name of the ship on which Charles Darwin made his voyage?",
                Arrays.asList("HMS Victory", "HMS Beagle", "HMS Endeavour", "HMS Bounty"),
                1, "History", "Medium"));
        
        addQuestion(new Question(null, "Which war was fought between the North and South in America?",
                Arrays.asList("Revolutionary War", "Civil War", "War of 1812", "Spanish-American War"),
                1, "History", "Medium"));
        
        // History - Hard (5 questions)
        addQuestion(new Question(null, "What was the capital of the Byzantine Empire?",
                Arrays.asList("Rome", "Athens", "Constantinople", "Alexandria"),
                2, "History", "Hard"));
        
        addQuestion(new Question(null, "Who was the last Pharaoh of Egypt?",
                Arrays.asList("Nefertiti", "Cleopatra VII", "Hatshepsut", "Ankhesenamun"),
                1, "History", "Hard"));
        
        addQuestion(new Question(null, "What treaty ended World War I?",
                Arrays.asList("Treaty of Versailles", "Treaty of Paris", 
                            "Treaty of Vienna", "Treaty of Westphalia"),
                0, "History", "Hard"));
        
        addQuestion(new Question(null, "Which dynasty built the Forbidden City in Beijing?",
                Arrays.asList("Tang Dynasty", "Song Dynasty", "Ming Dynasty", "Qing Dynasty"),
                2, "History", "Hard"));
        
        addQuestion(new Question(null, "What was the name of the pandemic that swept Europe in the 14th century?",
                Arrays.asList("Spanish Flu", "Black Death", "Cholera", "Smallpox"),
                1, "History", "Hard"));
        
        // Geography - Easy (5 questions)
        addQuestion(new Question(null, "What is the capital of France?",
                Arrays.asList("London", "Berlin", "Paris", "Madrid"),
                2, "Geography", "Easy"));
        
        addQuestion(new Question(null, "Which is the largest ocean?",
                Arrays.asList("Atlantic", "Indian", "Arctic", "Pacific"),
                3, "Geography", "Easy"));
        
        addQuestion(new Question(null, "How many continents are there?",
                Arrays.asList("5", "6", "7", "8"),
                2, "Geography", "Easy"));
        
        addQuestion(new Question(null, "What is the tallest mountain in the world?",
                Arrays.asList("K2", "Mount Everest", "Kangchenjunga", "Makalu"),
                1, "Geography", "Easy"));
        
        addQuestion(new Question(null, "Which country is known as the Land of the Rising Sun?",
                Arrays.asList("China", "Japan", "South Korea", "Thailand"),
                1, "Geography", "Easy"));
        
        // Geography - Medium (5 questions)
        addQuestion(new Question(null, "What is the longest river in the world?",
                Arrays.asList("Amazon", "Nile", "Yangtze", "Mississippi"),
                1, "Geography", "Medium"));
        
        addQuestion(new Question(null, "Which country has the most time zones?",
                Arrays.asList("Russia", "USA", "China", "France"),
                3, "Geography", "Medium"));
        
        addQuestion(new Question(null, "What is the driest desert in the world?",
                Arrays.asList("Sahara", "Gobi", "Atacama", "Kalahari"),
                2, "Geography", "Medium"));
        
        addQuestion(new Question(null, "Which strait separates Europe and Africa?",
                Arrays.asList("Strait of Gibraltar", "Bosphorus Strait", 
                            "Strait of Hormuz", "Strait of Malacca"),
                0, "Geography", "Medium"));
        
        addQuestion(new Question(null, "What is the capital of Australia?",
                Arrays.asList("Sydney", "Melbourne", "Canberra", "Perth"),
                2, "Geography", "Medium"));
        
        // Geography - Hard (5 questions)
        addQuestion(new Question(null, "What is the smallest country in the world?",
                Arrays.asList("Monaco", "Vatican City", "San Marino", "Liechtenstein"),
                1, "Geography", "Hard"));
        
        addQuestion(new Question(null, "Which lake is the deepest in the world?",
                Arrays.asList("Lake Superior", "Lake Baikal", "Caspian Sea", "Lake Tanganyika"),
                1, "Geography", "Hard"));
        
        addQuestion(new Question(null, "What is the southernmost capital city in the world?",
                Arrays.asList("Wellington", "Canberra", "Buenos Aires", "Cape Town"),
                0, "Geography", "Hard"));
        
        addQuestion(new Question(null, "Which country has the most natural lakes?",
                Arrays.asList("Finland", "Canada", "Russia", "Sweden"),
                1, "Geography", "Hard"));
        
        addQuestion(new Question(null, "What is the highest waterfall in the world?",
                Arrays.asList("Niagara Falls", "Victoria Falls", "Angel Falls", "Iguazu Falls"),
                2, "Geography", "Hard"));
        
        System.out.println("✅ " + questions.size() + " sample questions loaded successfully!");
    }
}