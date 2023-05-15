public class Medic implements Role {
    private String name;

    public Medic(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "The Medic can remove all cubes of a cured disease from a city by performing the Treat Disease action.";
    }

}