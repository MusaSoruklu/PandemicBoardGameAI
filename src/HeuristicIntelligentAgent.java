import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of the IntelligentAgent interface that uses a heuristic function to select
 * the best action sequence from the available actions.
 */
public class HeuristicIntelligentAgent implements IntelligentAgent {
    private static final int MAX_DEPTH = 3;

    /**
     * Selects the best action sequence from the list of available actions using a heuristic evaluation function.
     * The method searches for the best sequence of actions by performing a depth-limited search of the game tree.
     *
     * @param availableActions A list of available actions for the player to choose from.
     * @param gameState        The current state of the game.
     * @param player           The player whose turn it is.
     * @return The best sequence of actions to perform.
     */
    @Override
    public List<Action> selectActionSequence(List<Action> availableActions, GameState gameState, Player player) {
        List<Action> bestActionSequence = new ArrayList<>();
        int bestHeuristicValue = Integer.MIN_VALUE;

        // Try different depths of the search tree to find the best action sequence
        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            List<Action> actionSequence = searchActionSequence(availableActions, gameState, 0, depth, 4);
            int heuristicValue = gameState.heuristicEvaluation(null); // Pass null for the action parameter

            if (heuristicValue > bestHeuristicValue) {
                bestHeuristicValue = heuristicValue;
                bestActionSequence = actionSequence;
            }
        }

        return bestActionSequence;
    }

    /**
     * Searches for the best sequence of actions by performing a depth-limited search of the game tree.
     *
     * @param availableActions   A list of available actions for the player to choose from.
     * @param gameState          The current state of the game.
     * @param currentDepth       The current depth in the game tree.
     * @param maxDepth           The maximum depth to search in the game tree.
     * @param actionsRemaining   The number of actions remaining at the current depth.
     * @return The best sequence of actions to perform.
     */
    public List<Action> searchActionSequence(List<Action> availableActions, GameState gameState, int currentDepth, int maxDepth, int actionsRemaining) {
        if (currentDepth >= maxDepth || availableActions.isEmpty() || actionsRemaining <= 0) {
            return new ArrayList<>();
        }

        int bestHeuristicValue = Integer.MIN_VALUE;
        List<Action> bestActionSequence = null;

        // Try each available action to find the best one
        for (Action action : availableActions) {
            GameState newState = gameState.copy();
            action.perform(newState.getBoard(), newState.getCurrentPlayer());

            gameState.updateLastTwoActions(action);
            // Update available actions after performing the action
            List<Action> updatedAvailableActions = newState.getAvailableActions();

            List<Action> remainingActionSequence = searchActionSequence(updatedAvailableActions, newState, currentDepth + 1, maxDepth, actionsRemaining - 1);
            int heuristicValue = newState.heuristicEvaluation(action); // Pass the action to heuristicEvaluation

            // Update the best action sequence if this action leads to a better heuristic value
            if (heuristicValue > bestHeuristicValue) {
                bestHeuristicValue = heuristicValue;
                bestActionSequence = new ArrayList<>(Collections.singletonList(action));
                bestActionSequence.addAll(remainingActionSequence);
            }
        }

        // Return the best action sequence found
        return bestActionSequence != null ? bestActionSequence : new ArrayList<>();
    }
}