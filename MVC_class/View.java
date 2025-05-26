package MVC_class;

import MVC_class.Model.QuizModel.Question;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class View {
    // Cores ANSI para formatação de texto no método showColouredMessage
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    // Método para limpar a tela do console
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    // Método para exibir questões coletadas por uma lista de questões
    public static void showQuestions(List<Question> questions) { 
        Iterator<Question> iterator = questions.iterator();
        int counter = 1;
    
        while(iterator.hasNext()) { 
            Question question = iterator.next();
            System.out.println("Questão " + counter++ + ": " + question.text);
            question.options.forEach(alternative -> System.out.println((question.options.indexOf(alternative) + 1) + ") " + alternative));
            System.out.println("Resposta correta: " + question.correctAnswer);
            System.out.println("Data de criação: " + question.creationDate + "\n");
        }
    }

    // Método para exibir as opções iniciais do menu
    public static void showMenuoptions() { 
        showColouredMessage("Seja bem vindo!", "blue");
        System.out.println("Selecione uma das 3 opções abaixo: ");
        System.out.println("1 - Questionário interativo");
        System.out.println("2 - Mostrar questões filtradas por intervalo de data");
        System.out.println("3 - Mostrar plano de estudos");
        System.out.println("4 - Sair");
        System.out.print("Digite a opção desejada: ");
    }

    // Método para exibir um aviso de entrada inválida no menu
    public static void showInvalidMenuInputWarning() { 
        showColouredMessage("\nOpção inválida! Por favor, digite uma opção de 1 a 4.\n", "red");
    }

    // Método que imprime mensagens coloridas no console
    public static void showColouredMessage(String message, String color) {
        switch (color.toLowerCase()) {
            case "red":
                System.out.println(RED + message + RESET);
                break;
            case "green":
                System.out.println(GREEN + message + RESET);
                break;
            case "yellow":
                System.out.println(YELLOW + message + RESET);
                break;
            case "blue":
                System.out.println(BLUE + message + RESET);
                break;
            default: 
                System.out.println("Opção de cor inválida! Mensagem: " + message);
                break;
        }
    }

    public static class DateFilter { 
        public static void showDateFilterIntroduction() { 
            View.clearScreen();
            System.out.println("Bem vindo ao filtro de data!");
            showInputDateIntervalMessage();
        }
        public static void showInvalidDateIntervalWarning() { 
            // clearScreen();
            showColouredMessage("\nA data inserida é inválida! Insira novamente no seguinte modelo: 01/01/2024,01/01/2025\n", "red");
        }
        public static void showInputDateIntervalMessage() { 
            System.out.print("Digite o intervalo de data (ex.: 01/01/2024 - 01/01/2025) para as questões que gostaria de ver: ");
        }
    }

    public static class QuizView {
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
            return Controller.nextAnswer();
        }
    }
}
