public class InfectionManager {
    // Class attributes
    private Board board;
    private int infectionRateIndex;
    private static final int[] INFECTION_RATES = {2, 2, 2, 3, 3, 4, 4};
    private static final int[] INITIAL_INFECTION_LEVELS = {3, 3, 3, 2, 2, 2, 1, 1, 1};

    // Constructor
    public InfectionManager(Board board) {
        this.board = board;
        this.infectionRateIndex = 0;
    }

    // Infect multiple cities based on the current infection rate
    public void infectCities() {
        for (int i = 0; i < INFECTION_RATES[infectionRateIndex]; i++) {
            City city = board.getDeckManager().drawInfectionCard().getCity();
            String color = city.getColor();
            infectCity(city, color, 1);
        }
    }

    // Infect a specific city with a given number of disease cubes
    public void infectCity(City city, String color, int numCubes) {
        Disease disease = board.getDisease(color);

        // Skip infection if the disease has been eradicated
        if (disease.isEradicated()) {
            System.out.println("Skipping infection of city: " + city.getName() + " due to the " + disease.getColor() + " disease being eradicated.");
            return;
        }

        int currentCubes = city.getNumDiseaseCubes(color);
        int newCubes = currentCubes + numCubes;

        // Handle outbreak if the new number of cubes exceeds the maximum infection rate
        if (newCubes > Board.getMaxInfectionRate()) {
            new Outbreak(board).handle(city, disease);
        } else {
            System.out.println("Infecting city: " + city.getName() + " with " + numCubes + " " + color + " cubes");
            city.addCubes(color, numCubes);
            disease.removeCubes(numCubes);
        }
    }

    // Infect initial cities at the start of the game
    public void infectInitialCities() {
        int[] initialInfectionLevels = INITIAL_INFECTION_LEVELS;

        for (int i = 0; i < initialInfectionLevels.length; i++) {
            CityCard card = board.getDeckManager().drawInfectionCard();
            City city = card.getCity();
            String color = city.getColor();
            int cubes = initialInfectionLevels[i];
            infectCity(city, color, cubes);
        }
    }

    // Get the current infection rate
    public int getInfectionRate() {
        return INFECTION_RATES[infectionRateIndex];
    }

    // Increase the infection rate index
    public void increaseInfectionRate() {
        if (infectionRateIndex < INFECTION_RATES.length - 1) {
            infectionRateIndex++;
        }
    }

    // Create a copy of the current InfectionManager for a new board
    public InfectionManager copy(Board newBoard) {
        InfectionManager newInfectionManager = new InfectionManager(newBoard);
        newInfectionManager.infectionRateIndex = this.infectionRateIndex;
        return newInfectionManager;
    }

}