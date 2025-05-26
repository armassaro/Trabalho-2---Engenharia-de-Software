package MVC_class;

import MVC_class.Model.QuizModel.Question;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class View {
    public static class DateFilter { 
        public static void showDateFilterIntroduction() { 
            View.clearScreen();
            System.out.println("Bem vindo ao filtro de data!");
            System.out.println("Digite o intervalo de data (ex.: 01/01/2024 - 01/01/2025) para as questões que gostaria de ver: ");
        }
        public static void showInvalidDateIntervalWarning() { 
            // clearScreen();
            System.out.println("A data inserida é inválida! Insira novamente no seguinte modelo: 01/01/2024,01/01/2025");
        }
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

    public static void showQuestions(List<Question> questions) { 
        Iterator<Question> iterator = questions.iterator();

        while(iterator.hasNext()) { 
            Question question = iterator.next();
            System.out.println(question.creationDate);
        }
    }
}
