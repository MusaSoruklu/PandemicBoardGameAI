public class OperationsExpert implements Role {

    private final String name;

    public OperationsExpert(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Can build a research station in the current city without discarding a City card.";
    }

}