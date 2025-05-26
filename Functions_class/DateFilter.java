package Functions_class;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import MVC_class.Model;
import MVC_class.Model.QuizModel;
// Aluno responsável pela classe: Arthur Romano Massaro, 2478374
// Padrão de projeto escolhido: Singleton
// Breve explicação sobre a função: A classe DateFilter é responsável por filtrar perguntas com base em um intervalo de tempo específico determinado pela entrada do usuário.

public class DateFilter {
    // Construtor privado
    private DateFilter() {}
    // Método de classe público simulando um construtor, onde o mesmo sempre retorna a mesma instância do objeto para qualquer lugar no código,
    // garantindo que sempre haja apenas uma instância de DateFilter no programa inteiro
    public static DateFilter getInstance() {
        return new DateFilter();
    }

    // Método privado para converter uma string de data para LocalDate, facilitando o filtro por tempo
    private LocalDate parseDate(String dateStr) { 
        return LocalDate.parse(dateStr.substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Método privado para converter uma string de intervalo de datas para um vetor de LocalDate
    private LocalDate[] parseDates(String datesStr) { 
        System.out.println(datesStr);
        // String str = datesStr.length() == 24 ? datesStr : (String) datesStr.subSequence(0, 23);
        String[] dates = datesStr.split(",");
        System.out.println(dates[0] + dates[1]);
        LocalDate[] localDates = { LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(dates[1], DateTimeFormatter.ofPattern("dd/MM/yyyy")) };
        return localDates;
    } 

    // Método para filtragem de questões baseado em intervalo de data
    public List<QuizModel.Question> filterQuestionsByDateInterval(List<QuizModel.Question> questions, String datesStr) {
        List<QuizModel.Question> filteredQuestions = new ArrayList<>();
        LocalDate[] parsedDates = parseDates(datesStr);

        for(QuizModel.Question question : questions) {
            LocalDate questionDate = parseDate(question.creationDate);
            
            // Se a data da questão está depois da data inicial e antes da data final, então a data da questão está no intervalo válido
            if(questionDate.isAfter(parsedDates[0]) && questionDate.isBefore(parsedDates[1]) || (questionDate.isEqual(parsedDates[0]) || questionDate.isEqual(parsedDates[1]))) {
                filteredQuestions.add(question);
            }
        }

        return filteredQuestions;
    }

    // Método para filtragem de questões guardadas em arquivo
    public List<QuizModel.Question> filterStoredQuestionsByDateInterval(String datesStr) throws ClassNotFoundException, IOException {
        List<QuizModel.Question> filteredQuestions = new ArrayList<>();
        List<QuizModel.Question> questions = Model.QuizModel.getAllQuestions("example");

        LocalDate[] parsedDates = parseDates(Model.DateFilter.dateInterval);

        for(QuizModel.Question question : questions) {
            LocalDate questionDate = parseDate(question.creationDate);
            
            // Se a data da questão está depois da data inicial e antes da data final, então a data da questão está no intervalo válido
            if(questionDate.isAfter(parsedDates[0]) && questionDate.isBefore(parsedDates[1]) || (questionDate.isEqual(parsedDates[0]) || questionDate.isEqual(parsedDates[1]))) {
                filteredQuestions.add(question);
            }
        }

        return filteredQuestions;
    }
}
