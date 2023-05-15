import java.util.List;

public interface IntelligentAgent {
    List<Action> selectActionSequence(List<Action> availableActions, GameState gameState, Player player);
}