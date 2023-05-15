public class EpidemicCard extends Card {
    public EpidemicCard() {
        super(-1, "Epidemic", "");
    }

    @Override
    public EpidemicCard copy() {
        return new EpidemicCard();
    }

}