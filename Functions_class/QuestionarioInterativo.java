package Functions_class;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class QuestionarioInterativo {
    private static final String API_KEY = "AIzaSyCzYcP0zEVCnOu8D1e7TtUc5WaQhYFQT9c";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String PROMPT = """
    
    Gere 5 perguntas objetivas sobre %s com 4 alternativas cada (A, B, C, D) e a resposta correta.
    Formato:
    1. Pergunta: [texto]
    A) [opção A]
    B) [opção B]
    C) [opção C]
    D) [opção D]
    Resposta: [letra]
    
    Retorne apenas o texto das perguntas, sem títulos ou comentários e a cada pergunta coloque a palavra Pergunta: 
    """;

    private List<Pergunta> todasPerguntas = new ArrayList<>();
    private final List<Pergunta> perguntasAcertadas = new ArrayList<>();
    private final List<Pergunta> perguntasErradas = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new QuestionarioInterativo().iniciarQuestionario();
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public void iniciarQuestionario() {
        try {
            System.out.println("Bem-vindo ao Questionário Interativo!");
            System.out.print("\nDigite o tema do questionário (ex: Java, História, Ciências): ");
            String tema = scanner.nextLine();

            System.out.println("\nGerando perguntas sobre " + tema + "...");
            String resposta = chamarGemini(String.format(PROMPT, tema));
            String textoResposta = extrairTextoResposta(resposta);
            
            String dataAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            todasPerguntas = parsearPerguntas(textoResposta, dataAtual);

            if (todasPerguntas.isEmpty()) {
                System.out.println("Não foi possível gerar perguntas. Tente novamente.");
                return;
            }

            int acertos = aplicarQuestionario();
            
            System.out.println("\nRESULTADO FINAL");
            System.out.println("Você acertou " + acertos + " de " + todasPerguntas.size() + " perguntas!");
            System.out.println("\n🔍 Respostas corretas:");
            mostrarRespostasCorretas();

            salvarArquivos();
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private String chamarGemini(String prompt) throws IOException {
        URI uri = URI.create(API_URL + "?key=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
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

    private String extrairTextoResposta(String jsonResposta) {
        int inicio = jsonResposta.indexOf("\"text\": \"") + 9;
        int fim = jsonResposta.indexOf("\"", inicio);
        
        if (inicio < 9 || fim < 0) {
            return "Não foi possível extrair o texto da resposta";
        }
        
        return jsonResposta.substring(inicio, fim)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }

    private List<Pergunta> parsearPerguntas(String texto, String dataCriacao) {
        List<Pergunta> perguntas = new ArrayList<>();
        String[] linhas = texto.split("\n");
        
        Pergunta atual = null;
        for (String linha : linhas) {
            linha = linha.trim();
            if (linha.startsWith("Pergunta:") || linha.matches("^\\d+\\. Pergunta:.*")) {
                atual = new Pergunta();
                atual.texto = linha.substring(linha.indexOf(":") + 1).trim();
                atual.dataCriacao = dataCriacao;
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

    private int aplicarQuestionario() {
        perguntasAcertadas.clear();
        perguntasErradas.clear();
        int acertos = 0;
        
        System.out.println("\nQUESTIONÁRIO:\n");
        
        for (int i = 0; i < todasPerguntas.size(); i++) {
            Pergunta p = todasPerguntas.get(i);
            System.out.println((i+1) + ". " + p.texto);
            System.out.println("   Data: " + p.dataCriacao);
            
            char letra = 'A';
            for (String alternativa : p.alternativas) {
                System.out.println("   " + letra + ") " + alternativa);
                letra++;
            }
            
            System.out.print("\nSua resposta (A-D): ");
            String respostaUsuario = scanner.nextLine().trim().toUpperCase();
            
            while (respostaUsuario.length() != 1 || !"ABCD".contains(respostaUsuario)) {
                System.out.print("Resposta inválida! Digite A, B, C ou D: ");
                respostaUsuario = scanner.nextLine().trim().toUpperCase();
            }
            
            if (respostaUsuario.equals(p.resposta)) {
                System.out.println("Correto!\n");
                perguntasAcertadas.add(p);
                acertos++;
            } else {
                System.out.println("Errado! A resposta correta era " + p.resposta + "\n");
                perguntasErradas.add(p);
            }
        }
        return acertos;
    }

    private void mostrarRespostasCorretas() {
        for (int i = 0; i < todasPerguntas.size(); i++) {
            Pergunta p = todasPerguntas.get(i);
            System.out.println((i+1) + ". " + p.texto);
            System.out.println("   Resposta correta: " + p.resposta + ") " + 
                p.alternativas.get(p.resposta.charAt(0) - 'A') + "\n");
        }
    }

    private void salvarArquivos() throws IOException {
        salvarListaEmArquivo(todasPerguntas, "todas_questoes.dat");
        salvarListaEmArquivo(perguntasAcertadas, "acertadas.dat");
        salvarListaEmArquivo(perguntasErradas, "erradas.dat");
        
        System.out.println("\nArquivos salvos com sucesso:");
        System.out.println("- todas_questoes.dat");
        System.out.println("- acertadas.dat");
        System.out.println("- erradas.dat");
    }

    private void salvarListaEmArquivo(List<Pergunta> lista, String nomeArquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(lista);
        }
    }

    public List<Pergunta> getTodasPerguntas() {
        return new ArrayList<>(todasPerguntas);
    }

    public List<Pergunta> getPerguntasAcertadas() {
        return new ArrayList<>(perguntasAcertadas);
    }

    public List<Pergunta> getPerguntasErradas() {
        return new ArrayList<>(perguntasErradas);
    }

    public static class Pergunta implements Serializable {
        private static final long serialVersionUID = 1L;
        String texto;
        List<String> alternativas = new ArrayList<>();
        String resposta;
        String dataCriacao;
    }
}
