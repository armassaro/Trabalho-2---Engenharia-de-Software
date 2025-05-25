package MVC_class;
import java.util.Scanner;

public class Controller {
    private static Scanner s = new Scanner(System.in);
    
    public static class TimeFilter {
        public static void nextTimeInterval() { 
            s.nextLine();
        }
    }

    public static int nextMenuOption() { 
        return s.nextInt();
    }

    public static String nextAnswer() { 
        return s.next();
    }
}
