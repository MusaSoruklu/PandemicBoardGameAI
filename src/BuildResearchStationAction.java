public class BuildResearchStationAction extends Action {

private final City city;

    public BuildResearchStationAction(City city) {
        super("Build Research Station on " + city.getName());
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    @Override
    public void perform(Board board, Player player) {
        City currentCity = player.getCurrentCity();
        if (!currentCity.hasResearchStation()) {
            currentCity.setResearchStation(true);
        }
    }

    @Override
    public Action copy(Board board, Player player) {
        return new BuildResearchStationAction(player.getCurrentCity());
    }

    @Override
    public String toString() {
        return "Build Research Station on " + city.getName();
    }

    @Override
    public String getName() {
        return "Build Research Station on " + city.getName();
    }
}