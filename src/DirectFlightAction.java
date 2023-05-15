public class DirectFlightAction extends Action {
    private PlayerCard playerCard;

    public DirectFlightAction(PlayerCard playerCard) {
        super("Direct flight to " + playerCard.getCity().getName());
        this.playerCard = playerCard;
    }

    public PlayerCard getPlayerCard() {
        return playerCard;
    }

    @Override
    public void perform(Board board, Player player) {
        City destinationCity = playerCard.getCity();
        player.setCurrentCity(destinationCity);
        player.removeCardFromHand(playerCard);
    }

    @Override
    public String getName() {
        return "Direct flight to " + playerCard.getCity().getName();
    }

    @Override
    public String toString() {
        return "DirectFlightAction " + playerCard.getCity().getName();
    }

    @Override
    public Action copy(Board board, Player player) {
        return new DirectFlightAction(this.playerCard);
    }
}