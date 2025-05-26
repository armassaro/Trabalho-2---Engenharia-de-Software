// Importando as classes do MVC
import Functions_class.QuestionarioInterativo;
import MVC_class.*;

public class Main {
    public static void main(String[] args) {
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

        QuestionarioInterativo questionario = new QuestionarioInterativo();
        questionario.iniciarQuestionario();

        switch(Controller.nextMenuOption()) { 
            // Caso 1: Questionário interativo
            case 1: 
            break;
            // Caso 2: Mostrar questões filtradas por intervalo de tempo
            case 2: 
                View.TimeFilter.showTimeFilterIntroduction();
                Controller.TimeFilter.nextTimeInterval();
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