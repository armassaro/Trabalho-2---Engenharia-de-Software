package Functions;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import MVC.Model;
import MVC.Model.QuizModel;

public class DateFilter {
    private static DateFilter dateFilterInstance = new DateFilter();

    // Construtor privado
    private DateFilter() {}

    // Método de classe público simulando um construtor, onde o mesmo sempre retorna a mesma instância do objeto para qualquer lugar no código,
    // garantindo que sempre haja apenas uma instância de DateFilter no programa inteiro
    public static DateFilter getInstance() {
        return dateFilterInstance;
    }

    // Método privado para converter uma string de data para LocalDate, facilitando o filtro por tempo
    private LocalDate parseDate(String dateStr) { 
        return LocalDate.parse(dateStr.substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Método privado para converter uma string de intervalo de datas para um vetor de LocalDate
    private LocalDate[] parseDates(String datesStr) { 
        String[] dates = datesStr.split(",");
        LocalDate[] localDates = { LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(dates[1], DateTimeFormatter.ofPattern("dd/MM/yyyy")) };
        return localDates;
    } 

    // Método para filtragem de questões baseado em intervalo de data
    // O método abaixo não utiliza as questões armazenadas em arquivo, mas é um método que disponibiliza a funcionalidade de 
    // filtro por data para possíveis próximas implementações do código, onde a funcionalidade de filtro fosse necessária
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
        // Obém todas as questões armazenadas em arquivo a partir de getAllQuestions
        return filterQuestionsByDateInterval(Model.QuizModel.getAllQuestions("example"), datesStr);
    }
}
