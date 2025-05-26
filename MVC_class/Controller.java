package MVC_class;
import MVC_class.Model.QuizModel;
import MVC_class.Model.QuizModel.Question;
import MVC_class.View.QuizView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private static final Scanner s = new Scanner(System.in);
    
    public static class TimeFilter {
        public static void nextTimeInterval() { 
            s.nextLine();
        }
    }
    public static int nextMenuOption() { 
        return s.nextInt();
    }

    public static String nextAnswer() { 
        return s.next();
    }

    
public static class QuizController {
        private static final List<Question> correctAnswers = new ArrayList<>();
        private static final List<Question> wrongAnswers = new ArrayList<>();

        public static void run() {
            try {
                QuizView.display("🔍 Welcome to Interactive Quiz!");
                String topic = QuizView.getInput("Enter the quiz topic: ");
                
                QuizView.display("\n🔍 Generating questions...");
                List<Question> questions = QuizModel.generateQuestions(topic);
                
                if (questions.isEmpty()) {
                    QuizView.display("❌ Error generating questions");
                    return;
                }

                int score = runQuiz(questions);
                
                QuizView.display("\n📊 Final Score: " + score + "/" + questions.size());
                showCorrectAnswers(questions);
                
                QuizModel.saveData(questions, "all_questions.dat");
                QuizModel.saveData(correctAnswers, "correct_answers.dat");
                QuizModel.saveData(wrongAnswers, "wrong_answers.dat");
                
            } catch (IOException e) {
                QuizView.display("❌ Error: " + e.getMessage());
            } finally {
                QuizView.close();
            }
        }

        private static int runQuiz(List<Question> questions) {
            correctAnswers.clear();
            wrongAnswers.clear();
            int score = 0;
            
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                QuizView.displayQuestion(i+1, q);
                
                String userAnswer;
                while (!(userAnswer = QuizView.getInput("\nYour answer (A-D): ")).matches("[A-D]")) {
                    QuizView.display("Invalid input! Please enter A, B, C or D:");
                }
                
                if (userAnswer.equals(q.correctAnswer)) {
                    QuizView.display("✅ Correct!\n");
                    correctAnswers.add(q);
                    score++;
                } else {
                    QuizView.display("❌ Wrong! Correct answer: " + q.correctAnswer + "\n");
                    wrongAnswers.add(q);
                }
            }
            return score;
        }

        private static void showCorrectAnswers(List<Question> questions) {
            QuizView.display("\n🔍 Correct Answers:");
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                QuizView.display(String.format("%d. %s → %s) %s", 
                    i+1, q.text, q.correctAnswer, 
                    q.options.get(q.correctAnswer.charAt(0) - 'A')));
            }
        }
    }

}
