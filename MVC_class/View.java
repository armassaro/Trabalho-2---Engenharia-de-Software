package MVC_class;

import MVC_class.Model.QuizModel.Question;
import java.util.Scanner;

public class View {
    public static class TimeFilter { 
        public static void showTimeFilterIntroduction() { 
            View.clearScreen();
            System.out.println("Bem vindo ao filtro de tempo!");
            System.out.println("Digite o intervalo de tempo para as questões que gostaria de ver: ");
        }
        // public static void showTimeFilteredQuestions(List<Question> questions) { 
        //     for(Question question : questions) { 
        //         System.out.println(question.getStatement());
        //         System.out.println(question.getAlternatives());
        //         System.out.println("Resposta correta: " + question.getCorrectAnswer());
        //         System.out.println("Resposta marcada: " + question.getAnswer());
        //     }
        // }
    }

    public static void showMenuoptions() { 
        System.out.println("Seja bem vindo!");
        System.out.println("Selecione uma das 3 opções abaixo: ");
        System.out.println("1 - Questionário interativo");
        System.out.println("2 - Mostrar questões filtradas por intervalo de tempo");
        System.out.println("3 - Mostrar plano de estudos");
        System.out.println("4 - Sair");
        System.out.println("Digite a opção desejada: ");
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    
    public static class QuizView {
        private static final Scanner scanner = new Scanner(System.in);

        public static void display(String message) {
            System.out.println(message);
        }

        public static void displayQuestion(int number, Question question) {
            System.out.printf("\n%d. %s [%s]\n", number, question.text, question.creationDate);
            char option = 'A';
            for (String opt : question.options) {
                System.out.printf("   %s) %s\n", option++, opt);
            }
        }

        public static String getInput(String prompt) {
            System.out.print(prompt);
            return scanner.nextLine().trim().toUpperCase();
        }

        public static void close() {
            scanner.close();
        }
    }


}
