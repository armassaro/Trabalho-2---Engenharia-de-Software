import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GeradorPlanoEstudos {

    public static void main(String[] args) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("erradas.dat"));
            String conteudoErradas = new String(bytes, StandardCharsets.ISO_8859_1);
            
            String planoEstudos = generateStudyPlan("Matéria", conteudoErradas);
            System.out.println("=== PLANO DE ESTUDOS DETALHADO ===");
            System.out.println(planoEstudos);
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String generateStudyPlan(String materia, String conteudoErradas) throws IOException {
        final String API_KEY = "AIzaSyCzYcP0zEVCnOu8D1e7TtUc5WaQhYFQT9c";
        final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

        URI uri = URI.create(API_URL + "?key=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String prompt = """
            Gere um plano de estudos COMPLETO E DETALHADO para %s com base nas seguintes anotações:
            
            %s
            
            O plano deve conter os seguintes elementos EM PROFUNDIDADE:
            
            1. ANÁLISE DAS DIFICULDADES:
               - Pontos fracos identificados
               - Conceitos mal compreendidos
               - Áreas que requerem atenção imediata
            
            2. TÓPICOS PRIORITÁRIOS:
               - Lista hierárquica dos tópicos mais importantes
               - Tempo estimado para cada tópico
               - Pré-requisitos para cada tópico
            
            3. CRONOGRAMA DETALHADO:
               - Plano semanal com divisão por dias
               - Objetivos diários específicos
               - Períodos de revisão programados
            
            4. RECURSOS RECOMENDADOS:
               - Livros (com capítulos específicos)
               - Vídeos (com links quando possível)
               - Exercícios práticos
               - Ferramentas online úteis
            
            5. MÉTODOS DE ESTUDO:
               - Técnicas específicas para cada tópico
               - Como montar resumos eficazes
               - Estratégias para resolver exercícios
               - Como fazer revisões espaçadas
            
            6. MONITORAMENTO DE PROGRESSO:
               - Como autoavaliar o aprendizado
               - Sinais de que está evoluindo
               - Quando revisitar tópicos
            
            Formate a resposta em MARKDOWN com:
            - Títulos e subtítulos claros
            - Listas organizadas
            - Destaques para informações importantes
            - Espaçamento adequado para legibilidade
            
            Seja extremamente detalhado e forneça o máximo de informações úteis possível.
            """.formatted(materia, conteudoErradas);

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
        
        // Extrai o texto da resposta JSON
        int start = response.indexOf("\"text\": \"") + 9;
        int end = response.indexOf("\"", start);
        return start >= 9 && end > start ? 
               response.substring(start, end)
                  .replace("\\n", "\n")
                  .replace("\\\"", "\"")
                  .replace("\\t", "\t") : 
               "Formato de resposta inesperado";
    }
}