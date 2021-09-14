import java.util.Scanner;

public class Minesweeper {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        Game game = new Game(sc);
        
        String ans = "y";
        
        while (ans.equalsIgnoreCase("y")) {
            // starts a session of the game
            game.start();
            
            // asks user if they want to play again
            System.out.println("\nWant to play again? (Y/N)");
            ans = sc.next();
            
            System.out.println();
        }
    }
}
