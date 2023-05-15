public abstract class Action {
    private final String name;

    public Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Action copy(Board board, Player player);

    public abstract void perform(Board board, Player player);
}