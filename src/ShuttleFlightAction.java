public class ShuttleFlightAction extends Action {
    private City destination;
    private City origin;

    public ShuttleFlightAction(City destination, City origin) {
        super("Shuttle Flight to " + destination.getColor() + " " + destination.getName());
        this.destination = destination;
        this.origin = origin;
    }

    public City getDestination() {
        return destination;
    }

    public City getOrigin() {return origin;}

    @Override
    public void perform(Board board, Player player) {
        player.setCurrentCity(destination);
    }

    @Override
    public String getName() {
        return "Shuttle Flight to " + destination.getColor() + " " + destination.getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public Action copy(Board board, Player player) {
        return new ShuttleFlightAction(destination, origin);
    }

}