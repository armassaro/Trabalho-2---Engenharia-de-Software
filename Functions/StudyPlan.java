package Functions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StudyPlan {
    public static String generateFromWrongAnswers() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("erradas.dat"));
        if (!Files.exists(Paths.get("erradas.dat"))) {
            System.out.println("erradas.dat não encontrado");
        }
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }
}