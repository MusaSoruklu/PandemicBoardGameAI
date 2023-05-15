public class ShareKnowledgeAction extends Action {
    private String name;
    private Player playerToGive;
    private Player currentPlayer;
    private PlayerCard playerCardToGive;

    public ShareKnowledgeAction(Player currentPlayer, Player playerToGive, PlayerCard playerCardToGive) {
        super("Share Knowledge");
        this.name = "Share Knowledge";
        this.currentPlayer = currentPlayer;
        this.playerToGive = playerToGive;
        this.playerCardToGive = playerCardToGive;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void perform(Board board, Player player) {
        currentPlayer.removeCardFromHand(playerCardToGive);
        playerToGive.addCardToHand(playerCardToGive);
    }

    public Player getPlayerToGive() {
        return playerToGive;
    }

    @Override
    public Action copy(Board board, Player player) {
        return new ShareKnowledgeAction(currentPlayer,playerToGive,playerCardToGive);
    }

    @Override
    public String toString() {
        return "Share Knowledge";
    }

    public PlayerCard getPlayerCardToGive() {
        return playerCardToGive;
    }
}