public class PassAction extends Action {

    public PassAction() {
        super("pass");
        // Constructor
    }

    @Override
    public void perform(Board board, Player player) {
        // Do nothing
    }

    @Override
    public String getName() {
        return "Pass";
    }

    @Override
    public Action copy(Board board, Player player) {
        return new PassAction();
    }

}