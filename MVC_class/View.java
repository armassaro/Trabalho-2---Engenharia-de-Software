package MVC_class;

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
}
