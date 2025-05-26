package Functions_class;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Functions_class.QuestionarioInterativo;

// Aluno responsável pela classe: Arthur Romano Massaro, 2478374
// Padrão de projeto escolhido: Factory
// Breve explicação sobre a função: A classe TimeFilter é responsável por filtrar perguntas com base em um intervalo de tempo específico determinado pela entrada do usuário.

public class TimeFilter {
    private List<QuestionarioInterativo.Pergunta> questions;
    private LocalDate initialDate, finalDate;

    public TimeFilter(List<QuestionarioInterativo.Pergunta> questions, String timeInterval) {
        this.questions = questions;
        
        // Realiza o split de string para atribuir data inicial e final
        String[] dates = timeInterval.split(" - ");
        int i = 0;
        for(String date : dates) { 
            LocalDate localDate = parseDate(date);
            if(i == 0) { 
                this.initialDate = localDate;
            } else {
                this.finalDate = localDate;
            }
        }

    }

    // Método privado para converter uma string de data para LocalDate, facilitando o filtro por tempo
    private LocalDate parseDate(String dateStr) { 
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public List<QuestionarioInterativo.Pergunta> filterByTimeInterval(List<QuestionarioInterativo.Pergunta> questions) {
        List<QuestionarioInterativo.Pergunta> filteredQuestions = new ArrayList<>();

        for(QuestionarioInterativo.Pergunta question : questions) {
            LocalDate questionDate = parseDate(question.getData());
            if((questionDate.isEqual(initialDate) || questionDate.isAfter(initialDate)) &&
               (questionDate.isEqual(finalDate) || questionDate.isBefore(finalDate))) {
                filteredQuestions.add(question);
            }
        }
    }
}
