public class TreatDiseaseAction extends Action {
    private String color;
    private Board board;
    private City city;

    public TreatDiseaseAction(String color, Board board, City city) {
        super("Treat Disease in " + city.getName());
        this.color = color;
        this.board = board;
        this.city = city;
    }

    @Override
    public String getName() {
        return "Treat Disease in " + city.getName();
    }

    public City getCity() {
        return city;
    }

    @Override
    public void perform(Board board, Player player) {
        City currentCity = player.getCurrentCity();
        Disease disease = board.getDisease(color);
        int numCubes = currentCity.getNumDiseaseCubes(color);
        if (player.getRole() instanceof Medic || disease.isCured()) {
            currentCity.removeCubes(board,color,numCubes);
            disease.addCubes(numCubes);
        } else {
            currentCity.removeCubes(board, color, 1);
            disease.addCubes(1);
            //add the disease cubes back to the main sack of cubes after removing them from the city
//            System.out.println(player.getName() + " has removed 1 cube from " + currentCity.getName() + " New number of " + color + " infection cubes is " + currentCity.getNumDiseaseCubes(color));
        }
    }

    @Override
    public String toString() {
        return "Treat Disease in " + city.getName();
    }

    @Override
    public Action copy(Board board, Player player) {
        return new TreatDiseaseAction(this.color,this.board,this.city);
    }

}