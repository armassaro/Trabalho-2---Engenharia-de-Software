package MVC_class;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Model { 
    public static class TimeFilter { 
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
                Generate 5 objective questions about %s with 4 options each (A, B, C, D) and the correct answer.
                Format:
                1. Question: [text]
                A) [option A]
                B) [option B]
                C) [option C]
                D) [option D]
                Answer: [letter]
                
                Return only the question text, without titles or comments and prefix each question with 'Question: ' 
                """.formatted(topic);

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
            return (start < 9 || end < 0) ? "Error" : json.substring(start, end)
                .replace("\\n", "\n").replace("\\\"", "\"");
        }

        private static List<Question> parseQuestions(String text, String date) {
            List<Question> questions = new ArrayList<>();
            Question current = null;
            
            for (String line : text.split("\n")) {
                line = line.trim();
                if (line.startsWith("Question:") || line.matches("^\\d+\\. Question:.*")) {
                    current = new Question();
                    current.text = line.substring(line.indexOf(":") + 1).trim();
                    current.creationDate = date;
                } 
                else if (line.matches("^[A-D]\\) .*") && current != null) {
                    current.options.add(line.substring(3).trim());
                } 
                else if (line.startsWith("Answer:")) {
                    if (current != null) {
                        current.correctAnswer = line.substring(line.indexOf(":") + 1).trim().toUpperCase();
                        questions.add(current);
                    }
                }
            }
            return questions;
        }
    }
}
