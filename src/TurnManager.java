import java.util.List;
import java.util.Scanner;

// This class manages the turns of the game, either for a human player or an AI player
public class TurnManager {
    private final Board board;
    private static final int MAX_OUTBREAKS = 8;

    public TurnManager(Board board) {
        this.board = board;
    }

    // Plays a turn for the current player
    public void playTurn(Scanner scanner) {
        // Set the number of actions for the current player to 4
        Player currentPlayer = this.board.getCurrentPlayer();
        currentPlayer.setNumActions(4);

        // Continue playing actions until the player runs out of actions
        while (currentPlayer.getNumActions() > 0) {
            if (currentPlayer.getName().equals("AI")) {
                playAITurn(currentPlayer);
            } else {
                playHumanTurn(scanner, currentPlayer);
            }
        }

        // Draw 2 player cards to the player's hand when they run out of actions
        for (int i = 0; i < 2; i++) {
            drawAndHandlePlayerCard(currentPlayer);
        }

        // Discard excess cards and infect cities if there is no One Quiet Night event in effect
        currentPlayer.discardExcessCards(board, board.getDeckManager());
        if (!this.board.isOneQuietNight()) {
            board.getInfectionManager().infectCities();
        } else {
            this.board.setOneQuietNight(false);
        }

        // Check the game status after the turn is complete
        boolean gameStatus = checkGameStatus();
        if (gameStatus) {
            if (this.board.isGameWon()) {
                System.out.println("Congratulations! You have won the game!");
            } else {
                System.out.println("Sorry, you have lost the game.");
            }
            System.exit(0);
        }
    }

    // Plays a turn for an AI player
    private void playAITurn(Player currentPlayer) {
        // Create a deep copy of the board for the AI to use
        Board aiBoard = board.copy();
        // Get the available actions for the AI player and create a GameState object
        List<Action> actions = currentPlayer.getAvailableActions(aiBoard);
        GameState gameState = new GameState(aiBoard);
        // Use the agent to select the best action sequence for the AI player
        List<Action> bestActionSequence = currentPlayer.agent.selectActionSequence(actions, gameState, currentPlayer);
        // Perform each action in the best action sequence on the AI board, and apply it to the main board
        for (Action action : bestActionSequence) {
            action.perform(aiBoard, currentPlayer);
            applyAIAction(board, aiBoard, action);
            System.out.println("Player " + currentPlayer.getName() + " has performed action: " + action.getName());
            currentPlayer.setNumActions(currentPlayer.getNumActions() - 1);
            // Stop the loop if there are no more available actions for the AI player
            if (currentPlayer.getNumActions() == 0) {
                break;
            }
        }
    }

    // Applies an AI action to the main board
    public void applyAIAction(Board mainBoard, Board aiBoard, Action action) {
        // Find the corresponding player in the main board
        Player mainPlayer = mainBoard.getPlayers().get(aiBoard.getCurrentPlayerIndex());

        // Create a copy of the AI action with the main board and main player
        Action mainAction = action.copy(mainBoard, mainPlayer);

        // Perform the action on the main board
        mainAction.perform(mainBoard, mainPlayer);
    }

    private void playHumanTurn(Scanner scanner, Player currentPlayer) {
        // Print a message to notify the player that it's their turn
        System.out.println(currentPlayer.getName() + ", it's your turn!");

        // Print the current city of the player
        System.out.println("Current City: " + currentPlayer.getCurrentCity().getColor() + " " + currentPlayer.getCurrentCity().getName());

        // Start a conversation with the player to handle their turn
        ConversationManager conversationManager = new ConversationManager(board, currentPlayer, currentPlayer.agent);
        conversationManager.startConversation(scanner);
    }

    public boolean checkGameStatus() {
        // Check if all diseases are cured
        boolean allDiseasesCured = this.board.getDiseases().stream().allMatch(Disease::isCured);
        if (allDiseasesCured) {
            this.board.setGameWon(true);
            return true;
        }

        // Check if the game is lost due to maximum outbreaks or running out of player cards
        if (board.getOutbreakLevel() >= MAX_OUTBREAKS || board.getDeckManager().getPlayerDeck().size() == 0) {
            board.setGameLost(true);
            return true;
        } else {
            // Check if any disease has run out of cubes
            for (Disease disease : board.getDiseases()) {
                if (disease.getNumCubes() <= 0) {
                    board.setGameLost(true);
                    return true;
                }
            }
        }

        // If none of the above conditions are met, return false
        return false;
    }

    private void drawAndHandlePlayerCard(Player currentPlayer) {
        // Draw a player card from the deck
        Card drawnCard = this.board.getDeckManager().drawPlayerCard();

        // Check if the drawn card is an Epidemic card
        if (drawnCard instanceof EpidemicCard) {
            // Handle the Epidemic card
            handleEpidemic();
            board.getDeckManager().discardPlayerCard(drawnCard);
        } else {
            // Add the drawn card to the player's hand
            currentPlayer.addCardToHand(drawnCard);
        }
    }

    private void handleEpidemic() {
        // Increase the infection rate
        board.getInfectionManager().increaseInfectionRate();

        // Draw the bottom card of the infection deck and infect that city
        City city = board.getDeckManager().drawBottomInfectionCard().getCity();
        System.out.println(board.getCurrentPlayer().getName() + " Has drawn an Epidemic card in " + city.getName());
        board.getInfectionManager().infectCity(city, city.getColor(), 3);

        // Intensify the infection
        board.getDeckManager().intensifyInfection();
    }

}
