package MVC;
import Functions.StudyPlan;
import MVC.Model.QuizModel;
import MVC.Model.QuizModel.Question;
import MVC.View.QuizView;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public abstract class Controller {
    private static final Scanner s = new Scanner(System.in);

    public static Integer nextMenuOption() {
        try {
            return Integer.valueOf(s.next());
        } catch (NumberFormatException e) {
            // Caso a entrada não seja um número inteiro, retorna -1
            return -1;
        }
    }



    public static String nextAnswer() { 
        String str = s.next();

        return str;
    }

    public static class DateFilter {
        public static void nextDateInterval() { 
            String str = s.next();
            String[] strList = str.split(",");
            // Realiza a checagem se a string de interavalo de data é válida
            // Caso for inválida, coloca null para a variável de interavalo de data presente no Model.DateFilter
            for (String strList1 : strList) {
                try {
                    LocalDate.parse(strList1, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }catch(Exception e) { 
                    Model.DateFilter.dateInterval =  null;
                    return;
                }
            }
            Model.DateFilter.dateInterval = str;
        }
    }
    
    public static class QuizController {
        private static final List<Question> correctAnswers = new ArrayList<>();
        private static final List<Question> wrongAnswers = new ArrayList<>();

        public static void run() {
            try {
                QuizView.display("🔍 Bem-vindo ao Questionário Interativo!");
                String topic = QuizView.getInput("Digite o tema do questionário: ");
                
                QuizView.display("\n🔍 Gerando perguntas...");
                List<Question> questions = QuizModel.generateQuestions(topic);
                
                if (questions.isEmpty()) {
                    QuizView.display("❌ Não foi possível gerar perguntas");
                    return;
                }

                int score = runQuiz(questions);
                
                QuizView.display("\n📊 RESULTADO FINAL");
                QuizView.display("Você acertou " + score + " de " + questions.size() + " perguntas!");
                showCorrectAnswers(questions);
                
                QuizModel.saveData(questions, "todas_questoes.dat");
                QuizModel.saveData(correctAnswers, "acertadas.dat");
                QuizModel.saveData(wrongAnswers, "erradas.dat");
                
            } catch (IOException e) {
                QuizView.display("❌ Error: " + e.getMessage());
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
                while (!(userAnswer = QuizView.getInput("\nSua resposta (A-D): ")).matches("[A-D]")) {
                    QuizView.display("Resposta inválida! Digite A, B, C ou D:");
                }
                
                if (userAnswer.equals(q.correctAnswer)) {
                    QuizView.display("✅ Correto!\n");
                    correctAnswers.add(q);
                    score++;
                } else {
                    QuizView.display("❌ Errado! Resposta correta: " + q.correctAnswer + "\n");
                    wrongAnswers.add(q);
                }
            }
            return score;
        }

        private static void showCorrectAnswers(List<Question> questions) {
            QuizView.display("\n🔍 Respostas Corretas:");
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                QuizView.display(String.format("%d. %s → %s) %s", 
                    i+1, q.text, q.correctAnswer, 
                    q.options.get(q.correctAnswer.charAt(0) - 'A')));
            }
        }
    }


public static class StudyPlanController {
    public static void run() {
        try {
            String wrongAnswersContent = StudyPlan.generateFromWrongAnswers();
            
            if (wrongAnswersContent == null || wrongAnswersContent.trim().isEmpty()) {
                View.StudyPlanView.showNoWrongAnswersMessage();
                waitForEnter();
                return;
            }
            
            View.StudyPlanView.showLoadingMessage();
            
            // Gera o plano de estudos
            String studyPlan = Model.StudyPlanModel.generateStudyPlan(wrongAnswersContent);
            
            // Exibe o plano
            View.StudyPlanView.showStudyPlan(studyPlan);
            waitForEnter();
            
        } catch (IOException e) {
            View.showColouredMessage("❌ Erro ao gerar plano: " + e.getMessage(), "red");
        }
    }
    
    private static void waitForEnter() {
        try {
            System.in.read();
        } catch (IOException e) {
            // Ignora erros ao esperar Enter
        }
    }
}
     





}
