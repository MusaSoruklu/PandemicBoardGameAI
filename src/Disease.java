public class Disease {
    public static final String[] COLORS = {"Blue", "Red", "Yellow", "Black"};
    private final String color;
    private int numCubes;
    private boolean cured;

    public Disease(String color,  int initialNumCubes) {
        if (initialNumCubes < 0) {
            throw new IllegalArgumentException("Initial number of cubes cannot be negative.");
        }
        this.color = color;
        this.numCubes = initialNumCubes;
        this.cured = false;
    }

    public String getColor() {
        return color;
    }

    public int getNumCubes() {
        return numCubes;
    }

    public void setNumCubes(int numCubes) {
        this.numCubes = numCubes;
    }

    public void addCubes(int numToAdd) {
        numCubes += numToAdd;
    }

    public void removeCubes(int numToRemove) {
        if (numCubes < numToRemove) {
           return;
        }
        numCubes -= numToRemove;
    }

    public boolean isCured() {
        return cured;
    }

    public void cure() {
        cured = true;
    }


    public boolean isEradicated() {
        return numCubes == 24 && cured;
    }

    public Disease copy() {
        Disease newDisease = new Disease(color, numCubes);
        newDisease.cured = this.cured;
        return newDisease;
    }


}