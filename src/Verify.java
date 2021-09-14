import java.util.Scanner;

public class Verify {
    // Verifies user is entering an integer
    public static int intVerify(String message, Scanner sc) {
        String input;
        
        System.out.println(message);
        
        input = sc.next();
        
        while (!isInt(input)) {
            System.out.println("\nError. Please enter an integer.\n");
            
            System.out.println(message);
        
            input = sc.next();
        }
        
        return Integer.parseInt(input);
    }
    
    // Verifies string can be pasted to int
    private static boolean isInt(String intTest) {
        try {
            Integer.parseInt(intTest);
            return true;
        } catch (NumberFormatException err) {
            return false;
        }
    }
}
