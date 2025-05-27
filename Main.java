import MVC.*;
import Functions.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        while (true) {
            // Mostra a mensagem inicial de introdução ao programa
            // Mensagem:
            // Seja bem vindo!
            // Selecione uma das 3 opções abaixo:
            // 1 - Questionário interativo
            // 2 - Mostrar questões filtradas por intervalo de tempo
            // 3 - Mostrar plano de estudos
            // 4 - Sair
            // Digite a opção desejada:
            View.showMenuoptions();
            switch (Controller.nextMenuOption()) {
                // Caso 1: Questionário interativo
                case 1 -> Controller.QuizController.run();
                // Caso 2: Mostrar questões filtradas por intervalo de tempo
                case 2 -> {
                    View.DateFilter.showDateFilterIntroduction();
                    Controller.DateFilter.nextDateInterval();

                    // Enquanto houver entrada de uma data inválida, o programa continua solicitando uma nova entrada
                    while (Model.DateFilter.dateInterval == null) {
                        View.DateFilter.showInvalidDateIntervalWarning();
                        View.DateFilter.showInputDateIntervalMessage();
                        Controller.DateFilter.nextDateInterval();
                    }

                    // DateFilter sendo construído a partir do método estático
                    DateFilter dateFilter = DateFilter.getInstance();

                    // Demonstra as questões a partir da lista de questões geradas pelo filtro
                    View.showQuestions(dateFilter.filterStoredQuestionsByDateInterval(Model.DateFilter.dateInterval));
                }
                    
                case 3 -> {
                    Controller.StudyPlanController.run();
                }
                    
                case 4 -> System.exit(0);
                    
                default -> View.showInvalidMenuInputWarning();
            }
        }
    }
}