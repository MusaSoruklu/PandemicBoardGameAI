import java.util.List;

public class CureDiseaseAction extends Action {
    private final String diseaseColor;
    private final List<Card> cardsToDiscard;

    public CureDiseaseAction(String diseaseColor, List<Card> cardsToDiscard) {
        super("Cure " + diseaseColor + " disease");
        this.diseaseColor = diseaseColor;
        this.cardsToDiscard = cardsToDiscard;
    }

    public String getDiseaseColor() {
        return diseaseColor;
    }

    @Override
    public void perform(Board board, Player player) {
        if (player.getCurrentCity().hasResearchStation()) {
            for (Card card : cardsToDiscard) {
                player.removeCardFromHand(card);
                board.getDeckManager().discardPlayerCard(card);
            }
            board.getDisease(diseaseColor).cure();
        } else {
            System.out.println("You need to be at a research station to cure a disease.");
        }
    }

    @Override
    public String toString() {
        return "Cure " + diseaseColor + " disease";
    }



    @Override
    public Action copy(Board newBoard, Player newPlayer) {
        return new CureDiseaseAction(this.diseaseColor, this.cardsToDiscard);
    }
}