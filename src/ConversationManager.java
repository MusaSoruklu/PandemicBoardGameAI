import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConversationManager {
    private final Board board;
    private final Player currentPlayer;
    private final IntelligentAgent agent;

    public ConversationManager(Board board, Player currentPlayer, IntelligentAgent agent) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.agent = agent;
    }

    public String processUserMessage(String message) {
        message = message.toLowerCase().trim();
        if (message.equals("help")) {
            return displayHelp();
        } else if (message.startsWith("move to")) {
            String cityName = message.substring(7).trim();
            return handleMoveTo(cityName);
        } else if (message.startsWith("drive to")) {
            String cityName = message.substring(8).trim();
            return handleMoveTo(cityName);
        } else if (message.startsWith("ferry to")) {
            String cityName = message.substring(8).trim();
            return handleMoveTo(cityName);
        } else if (message.startsWith("drive/ferry to")) {
            String cityName = message.substring(14).trim();
            return handleMoveTo(cityName);
        } else if (message.startsWith("fly to")) {
            String cityName = message.substring(6).trim();
            return handleDirectFlight(cityName);
        } else if (message.startsWith("direct flight to")) {
            String cityName = message.substring(16).trim();
            return handleDirectFlight(cityName);
        } else if (message.startsWith("charter to")) {
            String cityName = message.substring(10).trim();
            return handleCharterFlight(cityName);
        } else if (message.startsWith("shuttle to")) {
          String cityName = message.substring(10).trim();
            return handleShuttleFlight(cityName);
        } else if (message.startsWith("treat")) {
            return handleTreatDisease();
        } else if (message.startsWith("build")) {
            return handleBuildResearchStation();
        } else if (message.startsWith("share")) {
            return handleShareKnowledge();
        } else if (message.startsWith("cure")) {
            return handleCureDisease();
        } else if (message.startsWith("show")) {
            String question = message.substring(4).trim();
            return answerQuestion(question);
        } else if (message.startsWith("tutorial")) {
return tutorialLink();
        } else if (message.startsWith("recommend")) {
            return getAIRecommendation();
        } else {
            return "I'm sorry, I didn't understand your message. Type 'help' for a list of interactions that I am capable of ";
        }

    }

    private String displayHelp() {
        return "Here's a list of possible commands:\n"
                + "1. move to <city name>\n"
                + "2. drive to <city name>\n"
                + "3. ferry to <city name>\n"
                + "4. drive/ferry to <city name>\n"
                + "5. fly to <city name>\n"
                + "6. direct flight to <city name>\n"
                + "7. charter to <city name>\n"
                + "8. shuttle to <city name>\n"
                + "9. treat\n"
                + "10. build\n"
                + "11. share\n"
                + "12. cure\n"
                + "13. show <question>\n"
                + "14. recommend (this will recommend the current best move)\n"
                + "15. help\n"
                + "16. tutorial\n"
                + "\nYou can also ask questions (preceeded by 'show '), such as 'my location', 'hand', 'high risk cities', 'infected cities', 'research stations', 'discarded player cards', 'discarded infection cards', 'chance of drawing epidemic card', 'actions', 'cured diseases', and 'eradicated diseases'.";
    }

    private String getAvailableMoves() {
        List<Action> actions = currentPlayer.getAvailableActions(board);

        if (actions.isEmpty()) {
            return "There are no available moves for you.";
        }

        StringBuilder response = new StringBuilder("Currently available moves:\n");
        for (Action action : actions) {
            response.append(action.getName()).append("\n");
        }
        return response.toString();
    }

    private String tutorialLink() {
        return "On your turn, you can take up to 4 actions, such as moving to a city, treating a disease, or building a research station.\n" +
                "After your actions, you draw two player cards and resolve any epidemic cards that are drawn.\n" +
                "Finally, you infect new cities based on the current infection rate and the number of cities infected in the previous turn.\n" +
                "The game continues with each player taking their turn until either all diseases are cured, all 4 cures are discovered, or the game is lost due to too many outbreaks or too many disease cubes on the board.\n " +
                "you can find a link to the official rules here:\n" +
                "https://images-cdn.zmangames.com/us-east-1/filer_public/25/12/251252dd-1338-4f78-b90d-afe073c72363/zm7101_pandemic_rules.pdf";
    }

    private String handleMoveTo(String cityName) {
        City destinationCity = board.findCityByName(cityName);

        if (destinationCity == null) {
            return "City not found: " + cityName;
        }

        for (Action action : currentPlayer.getAvailableActions(board)) {
            if (action instanceof DriveFerryAction && ((DriveFerryAction) action).getCity().equals(destinationCity)) {
                action.perform(board, currentPlayer);
                return currentPlayer.getName() + " has moved to " + cityName;
            }
        }
        return "You cannot move to " + cityName + " currently";
    }

    private String handleDirectFlight(String cityName) {
        City destinationCity = board.findCityByName(cityName);

        if (destinationCity == null) {
            return "City not found: " + cityName;
        }

        for (Action action : currentPlayer.getAvailableActions(board)) {
            if (action instanceof DirectFlightAction && ((DirectFlightAction) action).getPlayerCard().getCity().getName().equalsIgnoreCase(cityName)) {
                action.perform(board, currentPlayer);
                return currentPlayer.getName() + " has directly flown to " + cityName;
            }
        }
        return "You cannot fly to " + cityName + " currently";
    }

    private String handleCharterFlight(String cityName) {
        City destinationCity = board.findCityByName(cityName);

        if (destinationCity == null) {
            return "City not found: " + cityName;
        }

        for (Action action : currentPlayer.getAvailableActions(board)) {
            if (action instanceof CharterFlightAction && ((CharterFlightAction) action).getDestinationCity().equals(destinationCity)) {
                action.perform(board, currentPlayer);
                return currentPlayer.getName() + " has chartered a flight to " + cityName;
            }
        }
        return "You cannot charter a flight to " + cityName + " currently";
    }

    private String handleShuttleFlight(String cityName) {
        City destinationCity = board.findCityByName(cityName);

        if (destinationCity == null) {
            return "City not found: " + cityName;
        }

        for (Action action : currentPlayer.getAvailableActions(board)) {
            if (action instanceof ShuttleFlightAction && ((ShuttleFlightAction) action).getDestination().equals(destinationCity)) {
                action.perform(board, currentPlayer);
                return currentPlayer.getName() + " has shuttle flown to " + cityName;
            }
        }
        return "You cannot charter a flight to " + cityName + " currently";
    }

    private String handleTreatDisease() {
        for (Action action : currentPlayer.getAvailableActions(board)) {
            if (action instanceof TreatDiseaseAction) {
                action.perform(board, currentPlayer);
                if (board.getDisease(((TreatDiseaseAction) action).getCity().getColor()).isCured()) {
                    return currentPlayer.getName() + " has removed all cubes from " + currentPlayer.getCurrentCity().getName() + " since it is cured. The new number of disease cubes on this city is " + currentPlayer.getCurrentCity().getNumDiseaseCubes(currentPlayer.getCurrentCity().getColor());
                } else {
                    return currentPlayer.getName() + " has treated one disease cube on " + currentPlayer.getCurrentCity().getName() + ". The new number of disease cubes on this city is " + currentPlayer.getCurrentCity().getNumDiseaseCubes(currentPlayer.getCurrentCity().getColor());
                }
            }
        }
        return "You cannot treat disease at this location currently.";
    }

    private String handleBuildResearchStation() {
        for (Action action : currentPlayer.getAvailableActions(board)) {
            if (action instanceof BuildResearchStationAction) {
                action.perform(board, currentPlayer);
                return currentPlayer.getName() + " has built a research station.";
            }
        }
        return "You cannot build a research station at this location currently.";
    }

    private String handleShareKnowledge() {
        Player targetPlayer = null;
        for (Player player : board.getPlayers()) {
            if (player != currentPlayer) {
                targetPlayer = player;
            }
        }


        for (Action action : currentPlayer.getAvailableActions(board)) {
            if (action instanceof ShareKnowledgeAction && ((ShareKnowledgeAction) action).getPlayerToGive().equals(targetPlayer)) {
                action.perform(board, currentPlayer);
                return currentPlayer.getName() + " has shared knowledge with " + targetPlayer.getName() + ".";
            } else if (action instanceof ShareKnowledgeAction && ((ShareKnowledgeAction) action).getPlayerToGive().equals(currentPlayer)) {
                action.perform(board, targetPlayer);
                assert targetPlayer != null;
                return targetPlayer.getName() + " has shared knowledge with " + currentPlayer.getName() + ".";
            }
        }
    return "Can't share knowledge";
    }

    private String handleCureDisease() {
        for (Action action : currentPlayer.getAvailableActions(board)) {
            if (action instanceof CureDiseaseAction) {
                action.perform(board, currentPlayer);
                return currentPlayer.getName() + " has cured a disease.";
            }
        }
        return "You cannot cure a disease at this location currently.";
    }

    private String answerQuestion(String question) {
        // Answer the question based on game state
        // You can add more specific questions and their corresponding responses here
        if (question.equalsIgnoreCase("my location")) {
            City playerLocation = currentPlayer.getCurrentCity();
            return "You are currently in " + playerLocation.getName() + ".";
        } else if (question.equalsIgnoreCase("hand")) {
            Player AIplayer = null;
            for (Player player : board.getPlayers()) {
                if (player.getName().equals("AI")) {
                    AIplayer = player;
                }
            }
            assert AIplayer != null;
            return "You have " + currentPlayer.getHand().size() + " cards in your hand which are: " + currentPlayer.getHand() + "\nAI Player has " + AIplayer.getHand().size() + " cards in its hand which are: " + AIplayer.getHand();

        } else if (question.equalsIgnoreCase("high risk cities")) {
            List<String> highRiskCities = new ArrayList<>();
            for (City city : board.getCities()) {
                if (city.isAtOutbreakRisk()) {
                    highRiskCities.add(city.getName());
                }
            }
            return "These are the cities with 3 infections: " + highRiskCities;

        } else if (question.equalsIgnoreCase("infected cities")) {
            StringBuilder stringInfectedCities = new StringBuilder();
            stringInfectedCities.append("Cities with Infections:\n");
            for (City city : board.getCities()) {
                boolean cityHasInfections = false;
                StringBuilder cityInfections = new StringBuilder();
                cityInfections.append(city.getName()).append(": ");

                for (String color : Disease.COLORS) {
                    int numCubes = city.getNumDiseaseCubes(color);
                    if (numCubes > 0) {
                        cityHasInfections = true;
                        cityInfections.append(color).append(" - ").append(numCubes).append(" cubes; ");
                    }
                }
                if (cityHasInfections) {
                    stringInfectedCities.append(cityInfections.toString().trim()).append("\n");
                }
            }
            return stringInfectedCities.toString();

        } else if (question.equalsIgnoreCase("research stations")) {
            StringBuilder stringResearchStations = new StringBuilder();
            stringResearchStations.append("\nResearch Stations:\n");
            for (City city : board.getCities()) {
                if (city.hasResearchStation()) {
                    stringResearchStations.append(city.getName()).append("\n");
                }
            }
            return stringResearchStations.toString();

        } else if (question.equalsIgnoreCase("discarded player cards")) {
            List<Card> discardedPlayerCards = board.getDeckManager().getPlayerDiscardPile();
            return "Discarded player cards are: " + discardedPlayerCards;

        } else if (question.equalsIgnoreCase("discarded infection cards")) {
            StringBuilder stringDiscardedInfectionCards = new StringBuilder();
            stringDiscardedInfectionCards.append("\nDiscarded Infection Cards:\n");
            for (Card card : board.getDeckManager().getInfectionDiscardPile()) {
                stringDiscardedInfectionCards.append(card.getName()).append("\n");
            }

            return stringDiscardedInfectionCards.toString();

        } else if (question.equalsIgnoreCase("chance of drawing epidemic card")) {
            double chance = board.getDeckManager().getEpidemicCardDrawChance();
            return "The chance of drawing an epidemic card is " + (chance*100) + "%";

        } else if (question.equalsIgnoreCase("actions")) {
            return getAvailableMoves();
        } else if (question.equalsIgnoreCase("cured diseases") || question.equalsIgnoreCase("eradicated diseases")) {

            StringBuilder stringCuredDiseases = new StringBuilder();
            stringCuredDiseases.append("\nCured diseases:\n");
            for (Disease disease : board.getDiseases()) {
                if (disease.isCured()) {
                    stringCuredDiseases.append(disease.getColor()).append("\n");
                }
            }
            stringCuredDiseases.append("\nEradicated diseases:\n");
            for (Disease disease : board.getDiseases()) {
                if (disease.isEradicated()) {
                    stringCuredDiseases.append(disease.getColor()).append("\n");
                }
            }
            return stringCuredDiseases.toString();
        } else {
            return "I'm sorry, I don't have an answer to your question.";
        }
    }

    private String getAIRecommendation() {
        // Call AI agent to recommend the best action sequence and return the response
        Board aiBoard = board.copy();
        GameState gameState = new GameState(aiBoard);
        List<Action> actions = currentPlayer.getAvailableActions(aiBoard);
        List<Action> bestActionSequence = agent.selectActionSequence(actions, gameState, currentPlayer);

        // Build a string containing the recommended actions in sequence
        StringBuilder response = new StringBuilder("AI recommended actions:\n");
        for (Action recommendedAction : bestActionSequence) {
            response.append(recommendedAction.getName()).append("\n");
        }
        return response.toString();
    }

    public void startConversation(Scanner scanner) {
        String input;
        // Continue to take input and process actions/messages until the player has no actions left
        while (currentPlayer.getNumActions() > 0) {
            System.out.println("Type an action, a message to the AI, or 'pass' to finish your turn:");
            input = scanner.nextLine();

            // If the player inputs 'pass', set the number of actions to 0 and end the turn
            if (input.equalsIgnoreCase("pass")) {
                currentPlayer.setNumActions(0);
                break;
            }

            // If the input starts with "AI: ", the user is messaging the AI
            if (input.startsWith("AI: ")) {
                String message = input.substring(4);
                String response = processUserMessage(message);
                System.out.println("AI: " + response);
            } else {
                // Otherwise, process the input as an action and update the game state accordingly
                String response = processUserMessage(input);
                if (response.startsWith(currentPlayer.getName() + " has")) {
                    currentPlayer.setNumActions(currentPlayer.getNumActions() - 1);
                }
                System.out.println(response);
                System.out.println("Actions remaining: " + currentPlayer.getNumActions());
            }
        }
    }
}