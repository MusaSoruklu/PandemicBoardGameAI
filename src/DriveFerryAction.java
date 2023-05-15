public class DriveFerryAction extends Action {
    private final City city;

    public DriveFerryAction(City city) {
        super("Drive/Ferry to " + city.getColor() + " " + city.getName());
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    @Override
    public void perform(Board board, Player player) {
        if (player.getNumActions() > 0) {
            player.setCurrentCity(city);

        } else {
            System.out.println("No actions remaining.");
        }
    }

    @Override
    public Action copy(Board board, Player player) {
        return new DriveFerryAction(this.city);
    }

    @Override
    public String toString() {
        return "Drive/Ferry to " + city.getColor() + " " + city.getName();
    }

    @Override
    public String getName() {
        return "Drive/Ferry to " + city.getName();
    }

}