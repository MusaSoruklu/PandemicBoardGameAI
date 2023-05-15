import java.util.*;

/**
 * City class represents a city in the Pandemic game. It stores information
 * about the city's disease cubes, neighbors, research station status, and
 * quarantine status.
 */

public class City {
    private final int id;
    private final String name;
    private final String color;
    private boolean outbreaked;
    private List<City> neighbors; // new List property
    private final Disease disease;
    private Map<String, Integer> diseaseCubes;
    private boolean hasResearchStation;

    /**
     * Constructor for City.
     *
     * @param id The city's unique identifier.
     * @param name The name of the city.
     * @param disease The Disease object associated with the city.
     */

    public City(int id, String name, Disease disease) {
        this.id = id;
        this.name = name;
        this.outbreaked = false;
        this.disease = disease;
        this.color = disease.getColor();
        this.neighbors = new ArrayList<>();

        this.diseaseCubes = new HashMap<>();
        this.diseaseCubes.put("Blue", 0);
        this.diseaseCubes.put("Yellow", 0);
        this.diseaseCubes.put("Black", 0);
        this.diseaseCubes.put("Red", 0);


    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public boolean isOutbroken() {
        return outbreaked;
    }

    public void setOutbroken(boolean outbreaked) {
        this.outbreaked = outbreaked;
    }

    public List<City> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(City city) {
        neighbors.add(city);
    }

    public int getNumDiseaseCubes(String color) {
        return diseaseCubes.get(color);
    }

    public void addCubes(String color, int numCubes) {
        int newCityCubes = this.diseaseCubes.get(color) + numCubes;
        diseaseCubes.put(color, newCityCubes);
    }

    public void removeCubes(Board board, String color, int numCubes) {
        Disease disease = board.getDisease(color);
        if (disease.getNumCubes() - numCubes < 0) {
            disease.setNumCubes(0);
        } else {
            disease.removeCubes(numCubes);
        }

        // Update the diseaseCubes map in the City object
        int currentCityCubes = diseaseCubes.get(color);
        int newCityCubes = currentCityCubes - numCubes;
        if (newCityCubes < 0) {
            newCityCubes = 0;
        }
        diseaseCubes.put(color, newCityCubes);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public boolean isAtOutbreakRisk() {
        return getNumDiseaseCubes(color) >= 3;
    }

    public void setResearchStation(boolean set) {
        hasResearchStation = set;
    }

    public boolean hasResearchStation() {
        return hasResearchStation;
    }

    public boolean isQuarantined() {
        return false;
    }

    public City copy(Map<Disease, Disease> copiedDiseases) {
        Map<City, City> copiedCities = new HashMap<>();
        return copy(copiedCities, copiedDiseases);
    }

    private City copy(Map<City, City> copiedCities, Map<Disease, Disease> copiedDiseases) {
        if (copiedCities.containsKey(this)) {
            return copiedCities.get(this);
        }

        Disease newDisease = copiedDiseases.get(disease);
        City newCity = new City(id, name, newDisease);
        copiedCities.put(this, newCity);

        newCity.neighbors = new ArrayList<>();
        newCity.hasResearchStation = hasResearchStation;
        newCity.diseaseCubes = new HashMap<>(diseaseCubes);

        for (City neighbor : neighbors) {
            newCity.neighbors.add(neighbor.copy(copiedCities, copiedDiseases));
        }

        return newCity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("City: ").append(name).append(", Color: ").append(color).append(", Infections: ");
        for (Map.Entry<String, Integer> entry : diseaseCubes.entrySet()) {
            if (entry.getValue() > 0) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
            }
        }
        sb.append("Research Station: ").append(hasResearchStation() ? "Yes" : "No");
        return sb.toString();
    }

}