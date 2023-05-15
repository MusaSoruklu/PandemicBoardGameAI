import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Pandemic! What's your name?");
        String playerName = scanner.nextLine();
        System.out.println("Hello " + playerName + "! Press [enter] to start a game.");
        scanner.nextLine(); // Wait for user to press enter

        // Initialize game state
        board.setup();
        board.getPlayers().get(1).setName(playerName);

        // Start the game
        board.startGame(scanner);

        System.out.println("Game over!");
        scanner.close();
    }
}