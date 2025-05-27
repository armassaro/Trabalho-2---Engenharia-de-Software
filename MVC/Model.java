package MVC;

import MVC.Model.QuizModel.Question;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Model {
    public static class DateFilter {
        public static String dateInterval = null;
    }

    public static class QuizModel {
        private static final String API_KEY = "AIzaSyCzYcP0zEVCnOu8D1e7TtUc5WaQhYFQT9c";
        private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

        public static class Question implements Serializable {
            private static final long serialVersionUID = 1L;
            public String text;
            public List<String> options = new ArrayList<>();
            public String correctAnswer;
            public String creationDate;
        }

        public static List<Question> generateQuestions(String topic) throws IOException {
            String response = callAPI(topic);
            String responseText = extractText(response);
            return parseQuestions(responseText, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        }

        private static String callAPI(String topic) throws IOException {
            URI uri = URI.create(API_URL + "?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = """
                    Gere 5 perguntas objetivas sobre %s com 4 alternativas cada (A, B, C, D) e a resposta correta.
                    Formato:
                    1. Pergunta: [texto]
                    A) [opção A]
                    B) [opção B]
                    C) [opção C]
                    D) [opção D]
                    Resposta: [letra]

                    Retorne apenas o texto das perguntas, sem títulos ou comentários e a cada pergunta coloque a palavra Pergunta:
                    """
                    .formatted(topic);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}",
                        prompt.replace("\"", "\\\"")).getBytes("UTF-8"));
            }

            if (conn.getResponseCode() != 200) {
                throw new IOException("HTTP " + conn.getResponseCode() + ": " + conn.getResponseMessage());
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            return response.toString();
        }

        public static void saveData(List<Question> questions, String filename) throws IOException {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(questions);
            }
        }

        private static String extractText(String json) {
            int start = json.indexOf("\"text\": \"") + 9;
            int end = json.indexOf("\"", start);
            return (start < 9 || end < 0) ? "Erro"
                    : json.substring(start, end)
                            .replace("\\n", "\n").replace("\\\"", "\"");
        }

        private static List<Question> parseQuestions(String text, String date) {
            List<Question> questions = new ArrayList<>();
            Question current = null;

            for (String line : text.split("\n")) {
                line = line.trim();
                if (line.startsWith("Pergunta:") || line.matches("^\\d+\\. Pergunta:.*")) {
                    current = new Question();
                    current.text = line.substring(line.indexOf(":") + 1).trim();
                    current.creationDate = date;
                } else if (line.matches("^[A-D]\\) .*") && current != null) {
                    current.options.add(line.substring(3).trim());
                } else if (line.startsWith("Resposta:")) {
                    if (current != null) {
                        current.correctAnswer = line.substring(line.indexOf(":") + 1).trim().toUpperCase();
                        questions.add(current);
                    }
                }
            }
            return questions;
        }

        public static List<Question> getAllQuestions(String filename) throws IOException, ClassNotFoundException {
            List<Question> loadedQuestions = new ArrayList<>();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                // Faz o cast do objeto lido para List<Question>
                loadedQuestions = (List<Question>) ois.readObject();
            } catch (Exception e) {
            }

            return loadedQuestions;
        }
    }

    // INÍCIO DAS FUNÇÕES DE TESTE
    public static void generateMassiveQuestionFile(String filename) throws IOException {
        List<Question> massiveQuestions = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Gera 600 questões com datas variadas nos últimos 2 anos
        for (int i = 1; i <= 600; i++) {
            Question q = new Question();
            q.text = "Questão de exemplo #" + i + ": Qual é a capital do Brasil?";
            q.options.add("Rio de Janeiro");
            q.options.add("São Paulo");
            q.options.add("Brasília");
            q.options.add("Salvador");
            q.correctAnswer = "C";

            // Gera uma data aleatória nos últimos 2 anos
            q.creationDate = generateRandomDate(dateFormat);

            massiveQuestions.add(q);
        }

        // Salva no arquivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(massiveQuestions);
        }
    }

    private static String generateRandomDate(SimpleDateFormat dateFormat) {
        // Data atual
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();

        // Data 2 anos atrás
        cal.add(Calendar.YEAR, -2);
        Date startDate = cal.getTime();

        // Gera um timestamp aleatório entre startDate e endDate
        long randomMillis = ThreadLocalRandom.current()
                .nextLong(startDate.getTime(), endDate.getTime());

        return dateFormat.format(new Date(randomMillis));
    }
    // FIM DAS FUNÇÕES DE TESTE



   // No arquivo Model.java, adicione esta classe interna
public static class StudyPlanModel {
        private static final String API_KEY = "AIzaSyCzYcP0zEVCnOu8D1e7TtUc5WaQhYFQT9c";
        private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

        public static String generateStudyPlan(String content) throws IOException {
            String response = callAPI(content);
            return extractText(response);
        }

    private static String extractText(String json) {
            int start = json.indexOf("\"text\": \"") + 9;
            int end = json.indexOf("\"", start);
            return (start < 9 || end < 0) ? "Erro ao extrair texto" : 
                   json.substring(start, end)
                      .replace("\\n", "\n")
                      .replace("\\\"", "\"");
        }

    private static String callAPI(String content) throws IOException {
        URI uri = URI.create(API_URL + "?key=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String prompt = """
            Gere um plano de estudos COMPLETO E DETALHADO com base nas seguintes questões erradas:
            
            %s
            
            O plano deve conter:
            1. Tópicos prioritários para revisão
            2. Recursos recomendados (livros, vídeos, exercícios)
            3. Cronograma sugerido
            4. Métodos de estudo mais eficazes
            
            Formate a resposta em markdown com seções claras.
            """.formatted(content);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", 
                prompt.replace("\"", "\\\"")).getBytes(StandardCharsets.UTF_8));
        }

        if (conn.getResponseCode() != 200) {
            throw new IOException("Erro na API: " + conn.getResponseMessage());
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }
}






}


