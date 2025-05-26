// Importando as classes do MVC
import java.io.IOException;

import Functions_class.*;
import MVC_class.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
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

        
        while(true) { 
            switch(Controller.nextMenuOption()) { 
            // Caso 1: Questionário interativo
            case 1: 
                QuestionarioInterativo questionario = new QuestionarioInterativo();
                questionario.iniciarQuestionario();
            break;
            // Caso 2: Mostrar questões filtradas por intervalo de tempo
            case 2: 
            // Variável auxiliar para determinar qual data será digitada
                View.DateFilter.showDateFilterIntroduction();
                String dateInterval = Controller.DateFilter.nextDateInterval();
                
                while(dateInterval == null) { 
                    View.DateFilter.showInvalidDateIntervalWarning();
                    dateInterval = Controller.DateFilter.nextDateInterval();
                }
                // DateFilter sendo construído a partir do método estático
                DateFilter dateFilter = DateFilter.getInstance();

                // Demonstra as questões a partir da lista de questões geradas pelo filtro
                View.showQuestions(dateFilter.filterStoredQuestionsByDateInterval(Model.DateFilter.dateInterval));
            break;
            // Caso 3: Mostrar plano de estudos
            case 3: 
                
            break;
            // Caso 4: Sai do programa
            case 4:
                System.exit(0);
            break;
        }
    }
}
}