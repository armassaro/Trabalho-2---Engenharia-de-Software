import java.io.*;
import java.net.*;
import java.util.*;

public class QuestionarioInterativo {
    private static final String API_KEY = "AIzaSyCzYcP0zEVCnOu8D1e7TtUc5WaQhYFQT9c";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try (scanner) {
            System.out.println("🎯 QUESTIONÁRIO INTERATIVO 🎯");
            System.out.print("\nDigite o tema do questionário (ex: Java, História, Ciências): ");
            String tema = scanner.nextLine();

            System.out.println("\n🔍 Gerando perguntas sobre " + tema + "...");
            String resposta = chamarGemini(
                "Gere 5 perguntas objetivas sobre " + tema + " com 4 alternativas cada (A, B, C, D) e a resposta correta.\n" +
                "Formato:\n" +
                "1. Pergunta: [texto]\n" +
                "A) [opção A]\n" +
                "B) [opção B]\n" +
                "C) [opção C]\n" +
                "D) [opção D]\n" +
                "Resposta: [letra]\n\n" +
                "Retorne apenas o texto das perguntas, sem títulos ou comentários e a cada pergunta coloque a palavra Pergunta: "
            );
            String textoResposta = extrairTextoResposta(resposta);
            List<Pergunta> perguntas = parsearPerguntas(textoResposta);
            int acertos = aplicarQuestionario(perguntas);

            System.out.println("\n📊 RESULTADO FINAL");
            System.out.println("Você acertou " + acertos + " de " + perguntas.size() + " perguntas!");
            System.out.println("\n🔍 Respostas corretas:");
            mostrarRespostasCorretas(perguntas);

        } catch (IOException e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }

    private static String chamarGemini(String prompt) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(API_URL + "?key=" + API_KEY).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", 
            prompt.replace("\"", "\\\""));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes("UTF-8"));
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

    private static String extrairTextoResposta(String jsonResposta) {
        int inicio = jsonResposta.indexOf("\"text\": \"") + 9;
        int fim = jsonResposta.indexOf("\"", inicio);
        
        if (inicio < 9 || fim < 0) {
            return "Não foi possível extrair o texto da resposta";
        }
        
        return jsonResposta.substring(inicio, fim)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }

    private static List<Pergunta> parsearPerguntas(String texto) {
        List<Pergunta> perguntas = new ArrayList<>();
        String[] linhas = texto.split("\n");
        
        Pergunta atual = null;
        for (String linha : linhas) {
            linha = linha.trim();
            if (linha.startsWith("Pergunta:") || linha.matches("^\\d+\\. Pergunta:.*")) {
                atual = new Pergunta();
                atual.texto = linha.substring(linha.indexOf(":") + 1).trim();
            } else if (linha.matches("^[A-D]\\) .*")) {
                if (atual != null) {
                    atual.alternativas.add(linha.substring(3).trim());
                }
            } else if (linha.startsWith("Resposta:")) {
                if (atual != null) {
                    atual.resposta = linha.substring(linha.indexOf(":") + 1).trim().toUpperCase();
                    perguntas.add(atual);
                }
            }
        }
        return perguntas;
    }

    private static int aplicarQuestionario(List<Pergunta> perguntas) {
        int acertos = 0;
        System.out.println("\n📝 RESPONDA AS PERGUNTAS:\n");
        
        for (int i = 0; i < perguntas.size(); i++) {
            Pergunta p = perguntas.get(i);
            System.out.println((i+1) + ". " + p.texto);
            for (int j = 0; j < p.alternativas.size(); j++) {
                System.out.println((char)('A' + j) + ") " + p.alternativas.get(j));
            }
            
            System.out.print("Sua resposta (A-D): ");
            String respostaUsuario = scanner.nextLine().toUpperCase();
            
            if (respostaUsuario.equals(p.resposta)) {
                System.out.println("✅ Correto!\n");
                acertos++;
            } else {
                System.out.println("❌ Errado! A resposta correta era " + p.resposta + "\n");
            }
        }
        return acertos;
    }

    private static void mostrarRespostasCorretas(List<Pergunta> perguntas) {
        for (int i = 0; i < perguntas.size(); i++) {
            Pergunta p = perguntas.get(i);
            System.out.println((i+1) + ". " + p.texto);
            System.out.println("Resposta correta: " + p.resposta + ") " + 
                p.alternativas.get(p.resposta.charAt(0) - 'A') + "\n");
        }
    }

    static class Pergunta {
        String texto;
        List<String> alternativas = new ArrayList<>();
        String resposta;
    }
}