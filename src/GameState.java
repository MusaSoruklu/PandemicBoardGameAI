import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// This class represents the current state of the game, including the board, players,
// currentPlayer, and the last two actions performed by the currentPlayer.
public class GameState {
    private final Board board;
    private final List<Player> players;
    private final Player currentPlayer;
    private final Action[] lastTwoActions;

    // Constructor takes a Board object and initializes the GameState
    public GameState(Board board) {
        this.board = board.copy();
        this.players = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            this.players.add(this.board.getPlayers().get(i));
        }
        this.currentPlayer = this.board.getCurrentPlayer();
        lastTwoActions = new Action[2];
    }

    // Update the lastTwoActions array with a new action
    public void updateLastTwoActions(Action newAction) {
        lastTwoActions[0] = lastTwoActions[1];
        lastTwoActions[1] = newAction;
    }

    // Getter for the lastTwoActions array
    public Action[] getLastTwoActions() {
        return lastTwoActions;
    }

    // Getter for the board
    public Board getBoard() {
        return board;
    }

    // Getter for the currentPlayer
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // Returns a list of available actions for the currentPlayer
    public List<Action> getAvailableActions() {
        return currentPlayer.getAvailableActions(board);
    }


    // This method takes an action as input and evaluates the heuristic value of the GameState
    // after the action has been performed. It considers various factors such as cured diseases,
    // number of disease cubes on the board, AI player's cards, proximity to cities with 3 infection
    // cubes, and many others.

    public int heuristicEvaluation(Action actionTaken) {
        int heuristicValue = 0;

        // Factor 1: Number of cured diseases
        // Prioritize curing diseases by assigning a weight to the number of cured diseases on the board
        int curedDiseases = (int) board.getDiseases()
                .stream()
                .filter(Disease::isCured)
                .count();
        int curedDiseasesWeight = 100;
        heuristicValue += curedDiseases * curedDiseasesWeight;

        // Factor 2: Number of disease cubes on the board
        // Assign a negative weight to the total number of disease cubes on the board to discourage allowing disease cubes to accumulate
        int totalDiseaseCubes = board.getTotalDiseaseCubes();
        int diseaseCubesWeight = -100;
        heuristicValue += totalDiseaseCubes * diseaseCubesWeight;

        // Factor 3: Number of cards in the AI player's hand
        // Assign a weight to the number of cards in the AI player's hand to encourage using the cards to cure diseases or build research stations
        int playerCards = 0;
        for (Player player : players) {
            if (player.getName().equals("AI")) { // Use .equals() for string comparison
                playerCards = player.getHand().size();
                break;
            }
        }
        int aiPlayerCardsWeight = 10;
        heuristicValue += playerCards * aiPlayerCardsWeight;

        // Factor 4: Proximity to the closest city with 3 infection cubes
        // Assign a negative weight to the distance to the closest city with 3 infection cubes to encourage moving towards those cities to prevent outbreaks
        int closestCityWithThreeCubesDistance = Integer.MAX_VALUE;
        City currentPlayerCity = currentPlayer.getCurrentCity();
        City closestCityWithThreeCubes = null;
        for (City city : board.getCities()) {
            int cityInfectionLevel = city.getNumDiseaseCubes(city.getColor());
            if (cityInfectionLevel == 3) {
                int distance = board.getShortestPath(currentPlayerCity.getName(), city.getName()).size();
                if (distance < closestCityWithThreeCubesDistance) {
                    closestCityWithThreeCubesDistance = distance;
                    closestCityWithThreeCubes = city;
                }
            }
        }
        int cityWithThreeCubesProximityWeight = -500; // Adjust this value if necessary
        heuristicValue += closestCityWithThreeCubesDistance * cityWithThreeCubesProximityWeight;

        // Factor 5: Reward for actions that treat diseases
        // Assign a reward for using the TreatDiseaseAction to remove disease cubes from the current city
        if (actionTaken instanceof TreatDiseaseAction) {
            TreatDiseaseAction treatAction = (TreatDiseaseAction) actionTaken;
            City city = treatAction.getCity();
            if (city.equals(currentPlayerCity)) {
                int cityInfectionLevel = city.getNumDiseaseCubes(city.getColor());
                if (cityInfectionLevel > 0) {
                    int treatReward = 1000;
                    if (cityInfectionLevel == 3) {
                        treatReward = 3000; // Higher priority for cities with 3 cubes
                    }
                    heuristicValue += treatReward;
                }
            }
        }

        // Factor 6: Disease outbreak risk
        // Calculate the number of cities at risk of an outbreak and apply a weight to the heuristic value
        // A higher outbreak risk should lead to a lower heuristic value
        // This factor encourages the agent to prioritize preventing outbreaks
        int outbreakRisk = 0;
        for (City city : board.getCities()) {
            if (city.isAtOutbreakRisk()) {
                outbreakRisk += 1;
            }
        }
        int outbreakRiskWeight = -200;
        heuristicValue += outbreakRisk * outbreakRiskWeight;

        // Factor 7: Chain reaction risk
        // Calculate the risk of a chain reaction outbreak and apply a weight to the heuristic value
        // A higher chain reaction risk should lead to a lower heuristic value
        // This factor encourages the agent to prioritize preventing chain reaction outbreaks
        int chainReactionRisk = board.getChainReactionRisk();
        int chainReactionRiskWeight = -300;
        heuristicValue += chainReactionRisk * chainReactionRiskWeight;

        // Factor 8: Card management
        // Calculate the number of cards held by the player that are towards a disease cure and apply a weight to the heuristic value
        // A higher number of cards towards a cure should lead to a higher heuristic value
        // This factor encourages the agent to prioritize collecting cards for disease cures
        int cardsTowardsCure = currentPlayer.getCardsTowardsCure();
        int cardsTowardsCureWeight = 200;
        heuristicValue += cardsTowardsCure * cardsTowardsCureWeight;

        // Factor 10: Reward for curing diseases
        // If the action taken is a CureDiseaseAction, calculate a reward for curing the disease and apply it to the heuristic value
        // If the player is at a research station and has 5 cards of the same color, apply a higher reward to the heuristic value
        // This factor encourages the agent to prioritize curing diseases
        if (actionTaken instanceof CureDiseaseAction) {
            CureDiseaseAction cureAction = (CureDiseaseAction) actionTaken;
            String color = cureAction.getDiseaseColor();
            if (!board.getDisease(color).isCured()) {
                int cureReward = 5000;

                // Prioritize CureDiseaseAction if the player is at a research station and has 5 cards of the same color
                int maxSameColorCards = currentPlayer.getHand().stream()
                        .collect(Collectors.groupingBy(Card::getColor, Collectors.counting()))
                        .values()
                        .stream()
                        .mapToInt(Long::intValue)
                        .max()
                        .orElse(0);

                boolean currentPlayerAtResearchStation = currentPlayer.getCurrentCity().hasResearchStation();
                if (currentPlayerAtResearchStation && maxSameColorCards == 5) {
                    cureReward = 10000;
                }

                heuristicValue += cureReward;
            }
        }

        // Factor 11: Reward for building research stations, penalize if too close to other research stations
        // If the action taken is a BuildResearchStationAction, calculate a reward for building a research station and apply it to the heuristic value
        // If the minimum distance to existing research stations is less than 3, apply a penalty to the heuristic value
        // This factor encourages the agent to prioritize building research stations, but also encourages spreading them out
        if (actionTaken instanceof BuildResearchStationAction) {
            BuildResearchStationAction buildAction = (BuildResearchStationAction) actionTaken;
            City city = buildAction.getCity();
            if (!city.hasResearchStation()) {
                int buildReward = 1000;
                int minDistanceToExistingResearchStations = board.minDistanceToResearchStations(city);
                int distanceReward = 100 * minDistanceToExistingResearchStations; // You can adjust the weight
                int minDistanceToResearchStationsWeight = -800; // Penalty weight for building too close to existing research stations
                int minDistanceToResearchStationsPenalty = 0; // Initialize penalty to 0
                if (minDistanceToExistingResearchStations < 3) { // If the minimum distance to existing research stations is less than 3, apply penalty
                    minDistanceToResearchStationsPenalty = minDistanceToResearchStationsWeight * (3 - minDistanceToExistingResearchStations);
                }
                heuristicValue += buildReward + distanceReward + minDistanceToResearchStationsPenalty;
            }
        }


        // Factor 12: Penalize actions that move the player too far from cities with 3 infection cubes
        // If the action taken is a movement action, calculate the distance to the closest city with 3 infection cubes and apply a penalty to the heuristic value
        // A longer distance to the closest city with 3 infection cubes should lead to a lower heuristic value
        // This factor encourages the agent to prioritize being close to cities with 3 infection cubes
        if (actionTaken instanceof DriveFerryAction || actionTaken instanceof DirectFlightAction || actionTaken instanceof CharterFlightAction || actionTaken instanceof ShuttleFlightAction) {
            int distanceToClosestCityWithThreeCubes = Integer.MAX_VALUE;
            if (currentPlayerCity != null && closestCityWithThreeCubes != null) {
                distanceToClosestCityWithThreeCubes = board.getShortestPath(currentPlayerCity.getName(), closestCityWithThreeCubes.getName()).size();
            }
            int distanceWeight = -50;
            heuristicValue += distanceToClosestCityWithThreeCubes * distanceWeight;

            // Penalize DirectFlightAction if it would have had 4 or 5 cards of the same color before the action
            if (actionTaken instanceof DirectFlightAction) {
                DirectFlightAction directFlightAction = (DirectFlightAction) actionTaken;
                Card usedCard = directFlightAction.getPlayerCard();
                String usedCardColor = usedCard.getColor();
                int sameColorCardsBeforeAction = currentPlayer.getHand().stream()
                        .filter(card -> card.getColor().equals(usedCardColor))
                        .mapToInt(card -> 1)
                        .sum() + 1; // Add 1 to account for the used card

                if (sameColorCardsBeforeAction >= 4) {
                    int directFlightPenalty = -2000;
                    heuristicValue += directFlightPenalty;
                }
            }
        }

        // Factor 13: Penalize using the Pass action
        // If the action taken is a PassAction, apply a penalty to the heuristic value
        // This factor encourages the agent to prioritize taking useful actions instead of passing
        if (actionTaken instanceof PassAction) {
            heuristicValue -= 2000;
        }

        // Factor 15: Penalize for shuttling back and forth between the same cities
        // If the action taken is a ShuttleFlightAction, check the previous two actions to see if they were also ShuttleFlightActions between the same cities
        // If this pattern is detected, apply a penalty to the heuristic value
        // This factor encourages the agent to prioritize useful movement instead of shuttling back and forth
        if (actionTaken instanceof ShuttleFlightAction) {
            ShuttleFlightAction shuttleAction = (ShuttleFlightAction) actionTaken;
            Action[] previousActions = this.getLastTwoActions();

            if (previousActions[0] != null && previousActions[1] != null &&
                    previousActions[0] instanceof ShuttleFlightAction && previousActions[1] instanceof ShuttleFlightAction) {
                ShuttleFlightAction prevShuttleAction1 = (ShuttleFlightAction) previousActions[0];
                ShuttleFlightAction prevShuttleAction2 = (ShuttleFlightAction) previousActions[1];

                if (shuttleAction.getOrigin().equals(prevShuttleAction1.getDestination()) &&
                        shuttleAction.getDestination().equals(prevShuttleAction1.getOrigin()) &&
                        prevShuttleAction1.getOrigin().equals(prevShuttleAction2.getDestination()) &&
                        prevShuttleAction1.getDestination().equals(prevShuttleAction2.getOrigin())) {
                    heuristicValue -= 5000;
                }
            }
        }

        // Factor 16: Penalize consecutive Direct Flight actions
        // If the action taken is a DirectFlightAction, check the previous action to see if it was also a DirectFlightAction
        // If this pattern is detected, apply a penalty to the heuristic value
        // This factor encourages the agent to prioritize useful movement instead of consecutive DirectFlightActions
        if (actionTaken instanceof DirectFlightAction) {
            Action[] previousActions = this.getLastTwoActions();
            if (previousActions[0] != null && previousActions[0] instanceof DirectFlightAction) {
                heuristicValue -= 3000;
            }
        }

        // Factor 17: Prioritize getting to a research station when the player has 5 cards of the same color
        // Calculate the maximum number of cards of the same color held by the player and check if it is 5
        // If it is, apply a reward to the heuristic value if the player is at a research station, or a penalty if they are not
        // This factor encourages the agent to prioritize getting to a research station when they have 5 cards of the same color
        int maxSameColorCards = currentPlayer.getHand().stream()
                .collect(Collectors.groupingBy(Card::getColor, Collectors.counting()))
                .values()
                .stream()
                .mapToInt(Long::intValue)
                .max()
                .orElse(0);

        if (maxSameColorCards == 5) {
            boolean currentPlayerAtResearchStation = currentPlayer.getCurrentCity().hasResearchStation();
            int researchStationPriority = currentPlayerAtResearchStation ? 5000 : -5000;
            heuristicValue += researchStationPriority;
        }


        if (actionTaken instanceof DirectFlightAction) {
            Action[] previousActions = this.getLastTwoActions();
            if (previousActions[0] != null && previousActions[0] instanceof DriveFerryAction) {
                heuristicValue -= 3000;
            }
        }

        if (actionTaken instanceof DriveFerryAction) {
            Action[] previousActions = this.getLastTwoActions();
            if (previousActions[0] != null && previousActions[0] instanceof DirectFlightAction) {
                heuristicValue -= 3000;
            }
        }

        return heuristicValue;

        // Additional notes: some of these factors are complementary (e.g. encouraging building research stations but penalizing building too close to
    }


    // Returns a copy of the current GameState
    public GameState copy() {
        Board boardCopy = this.board.copy();
        List<Player> playersCopy = new ArrayList<>(boardCopy.getPlayers());
        return new GameState(boardCopy);
    }
}