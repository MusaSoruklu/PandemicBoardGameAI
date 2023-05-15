import java.util.*;

public class Board {
    private static final int NUM_DISEASE_COLORS = 4;
    private static final int MAX_INFECTION_RATE = 3;

    private static final int MAX_NUM_CUBES_PER_COLOR = 24;
    private static final int INITIAL_NUM_CUBES_PER_COLOR = 96 / NUM_DISEASE_COLORS;

    private List<City> cities;
    private List<Player> players;
    private int currentPlayerIndex;
    private List<Disease> diseases;

    private InfectionManager infectionManager;
    private TurnManager turnManager;
    private DeckManager deckManager;

    private int outbreakLevel;

    private boolean oneQuietNight;

    private boolean gameWon;

    private boolean gameLost;


    public Board() {
        cities = new ArrayList<>();
        players = new ArrayList<>();
        diseases = new ArrayList<>();
        currentPlayerIndex = 0;

    }

    // getters and setters for cities, players, diseases, researchStations

    public List<City> getCities() {
        return cities;
    }

    public int getNumResearchStations() {
        int numberOfStations = 0;
        for (City city : cities) {
            if (city.hasResearchStation()) {
                numberOfStations++;
            }
        }
        return numberOfStations;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // Advance to the next player
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public InfectionManager getInfectionManager() {
        return infectionManager;
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }

    //gets max infection rate
    public static int getMaxInfectionRate() {
        return MAX_INFECTION_RATE;
    }

    //gets the current outbreak level
    public int getOutbreakLevel() {
        return outbreakLevel;
    }

    public void setOutbreakLevel(int outbreakLevel) {
        this.outbreakLevel = outbreakLevel;
    }

    public boolean isOneQuietNight() {
        return oneQuietNight;
    }

    public void setOneQuietNight(boolean oneQuietNight) {
        this.oneQuietNight = oneQuietNight;
    }

    // Calculate the minimum distance from the given city to any research station
    public int minDistanceToResearchStations(City city) {
        int minDistance = Integer.MAX_VALUE;
        for (City researchStationCity : cities) {
            if (researchStationCity.hasResearchStation()) {
                int distance = getShortestPath(city.getName(), researchStationCity.getName()).size();
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        return minDistance;
    }

    // Calculate the risk of chain reaction outbreak based on cities at outbreak risk
    public int getChainReactionRisk() {
        int chainReactionRisk = 0;
        for (City city : cities) {
            if (city.isAtOutbreakRisk()) {
                for (City neighbour : city.getNeighbors()) {
                    if (neighbour.isAtOutbreakRisk()) {
                        chainReactionRisk++;
                    }
                }
            }
        }
        return chainReactionRisk;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public void setGameLost(boolean gameLost) {
        this.gameLost = gameLost;
    }

    // Get the disease object by color
    public Disease getDisease(String color) {
        for (Disease disease : diseases) {
            if (disease.getColor().equals(color)) {
                return disease;
            }
        }
        return null;
    }

    //Get a list of the diseases (this contains the amount of cubes in the disease sack, not the disease cubes on a specific city)
    public List<Disease> getDiseases() {
        return diseases;
    }

    public int getTotalDiseaseCubes() {
        int totalCubes = 0;
        for (Disease disease : diseases) {
            int missingCubes = MAX_NUM_CUBES_PER_COLOR - disease.getNumCubes();
            totalCubes += missingCubes;
        }
        return totalCubes;
    }

    // Find a city by its name
    public City findCityByName(String cityName) {
        for (City city : cities) {
            if (city.getName().equalsIgnoreCase(cityName)) {
                return city;
            }
        }
        return null;
    }

    // Get the shortest path between two cities using BFS
    public List<City> getShortestPath(String sourceName, String destinationName) {
        City source = findCityByName(sourceName);
        City destination = findCityByName(destinationName);
        if (source.equals(destination)) {
            return Collections.singletonList(source);
        }

        Map<City, Boolean> visited = new HashMap<>();
        Map<City, City> prevCity = new HashMap<>();
        for (City c : cities) {
            visited.put(c, false);
        }

        Queue<City> queue = new LinkedList<>();
        queue.add(source);
        visited.put(source, true);

        while (!queue.isEmpty()) {
            City currentCity = queue.poll();

            for (City neighbor : currentCity.getNeighbors()) {
                if (visited.get(neighbor) == null || !visited.get(neighbor)) {
                    prevCity.put(neighbor, currentCity);
                    if (neighbor.equals(destination)) {
                        List<City> path = new ArrayList<>();
                        City pathCity = neighbor;
                        while (pathCity != null) {
                            path.add(pathCity);
                            pathCity = prevCity.get(pathCity);
                        }
                        Collections.reverse(path);
                        return path;
                    }
                    visited.put(neighbor, true);
                    queue.add(neighbor);
                }
            }
        }

        return Collections.emptyList(); // Return an empty list if there's no path
    }

    // Sets up the game board by initializing game components and game status
    public void setup() {

    // Instantiate the InfectionManager, TurnManager, and DeckManager
        infectionManager = new InfectionManager(this);
        turnManager = new TurnManager(this);
        deckManager = new DeckManager(this);

        // Set up game decks, place research stations, draw initial player cards, and infect initial cities
        initializeDiseases();
        initializeCities();
        initializePlayers();
        deckManager.createDecks();
        placeInitialResearchStations();
        deckManager.drawInitialPlayerCards();
        infectionManager.infectInitialCities();

        // Initialize game status variables
        outbreakLevel = 0;
        oneQuietNight = false;
        gameWon = false;
        gameLost = false;
    }

    // Initialize cities
    private void initializeCities() {
        this.cities = CityFactory.createCities(diseases);
    }

    // Initializes diseases by creating a Disease object for each color and adding them to the diseases list
    private void initializeDiseases() {
        for (int i = 0; i < NUM_DISEASE_COLORS; i++) {
            Disease disease = new Disease(Disease.COLORS[i], INITIAL_NUM_CUBES_PER_COLOR);
            diseases.add(disease);
        }
    }

    // Initializes players by creating roles, assigning them to the players, and adding the players to the players list
    private void initializePlayers() {
        // Create player roles
        List<Role> roles = new ArrayList<>();
        roles.add(new OperationsExpert("Operations Expert"));
        roles.add(new Medic("Medic"));
        // ... create more roles ...

        // Create players
        Player player = new Player("Human", cities.get(0), roles.get(0), new HeuristicIntelligentAgent());
        Player aiPlayer = new Player("AI", cities.get(0), roles.get(1), new HeuristicIntelligentAgent());
        players.add(aiPlayer);
        players.add(player);


    }

    // Places the initial research stations on the board in the starting city
    private void placeInitialResearchStations() {
        City startingCity = cities.get(0);
        startingCity.setResearchStation(true);
    }

    // Starts the game by playing turns until the game is won or lost, then displays the result
    public void startGame(Scanner scanner) {
        while (!isGameWon() && !isGameLost()) {
            turnManager.playTurn(scanner);
            nextPlayer();
        }

        if (isGameWon()) {
            System.out.println("You Won the Game! :)");
        } else {
            System.out.println("You Lost the Game.");
        }
    }


    // Create a deep copy of the board
    public Board copy() {
        Board newBoard = new Board();

        // Copy diseases
        List<Disease> newDiseases = new ArrayList<>();
        for (Disease disease : diseases) {
            newDiseases.add(disease.copy());
        }
        newBoard.diseases = newDiseases;

        // Create a copiedDiseases map
        Map<Disease, Disease> copiedDiseases = new HashMap<>();
        for (int i = 0; i < diseases.size(); i++) {
            copiedDiseases.put(diseases.get(i), newDiseases.get(i));
        }

        // Copy cities
        List<City> newCities = new ArrayList<>();
        for (City city : cities) {
            City copiedCity = city.copy(copiedDiseases);
            newCities.add(copiedCity);
        }
        newBoard.cities = newCities;

        // Copy players
        List<Player> newPlayers = new ArrayList<>();
        for (Player player : players) {
            newPlayers.add(player.copy(newBoard));
        }

        newBoard.players = newPlayers;
        newBoard.currentPlayerIndex = currentPlayerIndex;


        // Copy InfectionManager, TurnManager, and DeckManager
        newBoard.infectionManager = infectionManager.copy(newBoard);
        newBoard.turnManager = new TurnManager(newBoard);
        newBoard.deckManager = deckManager.copy(newBoard);

        // Copy outbreakLevel, oneQuietNight, gameWon, gameLost
        newBoard.outbreakLevel = outbreakLevel;
        newBoard.oneQuietNight = oneQuietNight;
        newBoard.gameWon = gameWon;
        newBoard.gameLost = gameLost;

        return newBoard;
    }

    // Convert the board's state to a string representation
    @Override
    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Game State:\n");
            sb.append("Infection Rate: ").append(infectionManager.getInfectionRate()).append("\n");
            sb.append("Outbreak Level: ").append(getOutbreakLevel()).append("\n\n");
            sb.append("Cities with Infections:\n");

            for (City city : cities) {
                boolean cityHasInfections = false;
                StringBuilder cityInfections = new StringBuilder();
                cityInfections.append(city.getName()).append(": ");

                for (String color : Disease.COLORS) {
                    int numCubes = city.getNumDiseaseCubes(color);
                    if (numCubes > 0) {
                        cityHasInfections = true;
                        cityInfections.append(color).append(" - ").append(numCubes).append(" cubes; ");
                    }
                }

                if (cityHasInfections) {
                    sb.append(cityInfections.toString().trim()).append("\n");
                }
            }

            sb.append("\nResearch Stations:\n");
            for (City city : cities) {
                if (city.hasResearchStation()) {
                    sb.append(city.getName()).append("\n");
                }
            }

            sb.append("\nPlayer Information:\n");
            for (Player player : players) {
                sb.append(player.getName())
                        .append(" - ")
                        .append(player.getRole().getName())
                        .append(" - ")
                        .append(player.getCurrentCity().getName())
                        .append("\n");
            }

            return sb.toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "Error in Board.toString(): " + e.getMessage();
        }
    }


}