public class Outbreak {
    // Class attributes
    private Board board;
    private static final int MAX_OUTBREAK_LEVEL = 8;

    // Constructor
    public Outbreak(Board board) {
        this.board = board;
    }

    // Handle an outbreak in a given city
    public void handle(City city, Disease disease) {
        // If the city has a research station or has already experienced an outbreak, return
        if (city.hasResearchStation() || city.isOutbroken()) {
            return;
        }

        // Increment the outbreak level and mark the city as outbroken
        int outbreakLevel = board.getOutbreakLevel();
        outbreakLevel++;
        city.setOutbroken(true);
        board.setOutbreakLevel(outbreakLevel);

        // If the outbreak level reaches or exceeds the maximum, set the game as lost
        if (outbreakLevel >= MAX_OUTBREAK_LEVEL) {
            board.setGameLost(true);
        } else {
            // If the outbreak level is below the maximum, infect neighboring cities
            System.out.println("An Outbreak has occurred in " + city.getName());
            String color = disease.getColor();
            for (City neighbor : city.getNeighbors()) {
                // Infect neighboring cities if they have not experienced an outbreak and are not quarantined
                if (!neighbor.isOutbroken() && !neighbor.isQuarantined()) {
                    board.getInfectionManager().infectCity(neighbor, color, 1);
                }
            }
        }
    }
}
