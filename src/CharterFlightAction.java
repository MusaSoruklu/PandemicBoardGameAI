public class CharterFlightAction extends Action {
    private final City destinationCity;

    public CharterFlightAction(City destinationCity) {
        super("Charter Flight");
        this.destinationCity = destinationCity;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    @Override
    public void perform(Board board, Player player) {
        Card destinationCard = player.findCardInHand(destinationCity.getName());
        player.setCurrentCity(destinationCity);
        player.removeCardFromHand(destinationCard);
    }

    @Override
    public Action copy(Board board, Player player) {
        return new CharterFlightAction(this.destinationCity);
    }

    @Override
    public String toString() {
        return "DirectFlightAction " + destinationCity.getName();
    }

    @Override
    public String getName() {
        return "Charter Flight to: " + destinationCity;
    }
}