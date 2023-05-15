import java.util.*;

public class DeckManager {
    private final Board board;
    private List<Card> playerDeck;
    private List<Card> infectionDeck;
    private List<Card> playerDiscardPile;
    private List<Card> infectionDiscardPile;

    private static final int NUM_EPIDEMIC_CARDS = 3;

    public DeckManager(Board board) {
        this.board = board;
        playerDeck = new ArrayList<>();
        infectionDeck = new ArrayList<>();
        playerDiscardPile = new ArrayList<>();
        infectionDiscardPile = new ArrayList<>();
    }

    public void createDecks() {
        createPlayerDeck();
        createInfectionDeck();
    }

    private void createPlayerDeck() {
        for (City city : board.getCities()) {
            PlayerCard playerCard = new PlayerCard(city.getId(), city.getName(), city.getColor(), city);
            playerDeck.add(playerCard);
        }

        Collections.shuffle(playerDeck);
    }

    private void createInfectionDeck() {
        for (City city : board.getCities()) {
            CityCard infectionCard = new CityCard(city.getId(), city.getName(), city.getColor(), city);
            infectionDeck.add(infectionCard);
        }

        Collections.shuffle(infectionDeck);
    }

    // Other methods for drawing, shuffling, and discarding cards

    public Card drawPlayerCard() {
        if (playerDeck.isEmpty()) {
            return null;
        }
        return playerDeck.remove(0);
    }

    public double getEpidemicCardDrawChance() {
        // Calculate the number of cards remaining in the playerDeck
        int remainingCards = playerDeck.size() - playerDiscardPile.size();

        // Calculate the number of epidemic cards remaining in the playerDeck
        int remainingEpidemicCards = NUM_EPIDEMIC_CARDS - (int) playerDiscardPile.stream()
                .filter(card -> card instanceof EpidemicCard)
                .count();

        // Calculate the chance of drawing an epidemic card
        double chance = (double) remainingEpidemicCards / (double) remainingCards;

        return chance;
    }


    public CityCard drawInfectionCard() {
        if (infectionDeck.isEmpty()) {
            return null;
        }
        CityCard card = (CityCard) infectionDeck.remove(0);
        discardInfectionCard(card);
        return card;
    }

    public void discardPlayerCard(Card card) {
        playerDiscardPile.add(card);
    }

    public void discardInfectionCard(CityCard card) {
        infectionDiscardPile.add(card);
    }

    public List<Card> getPlayerDeck() {
        return playerDeck;
    }

    public CityCard drawBottomInfectionCard() {
        if (infectionDeck.isEmpty()) {
            return null;
        }
        CityCard card = (CityCard) infectionDeck.remove(infectionDeck.size() - 1);
        discardInfectionCard(card);
        return card;
    }

    public void intensifyInfection() {
        shuffleDeck(infectionDiscardPile);
        infectionDeck.addAll(0, infectionDiscardPile);
        infectionDiscardPile.clear();
    }

    public void shuffleDeck(List<Card> deck) {
        Collections.shuffle(deck);
    }

    public void drawInitialPlayerCards() {
        int initialCardsPerPlayer = 4; // This value can be changed

        for (Player player : board.getPlayers()) {
            for (int i = 0; i < initialCardsPerPlayer; i++) {
                Card card = playerDeck.remove(0);
                player.addCardToHand(card);
            }
        }

        // Add Epidemic cards after drawing initial player cards
        insertEpidemicCards();
    }

    private void insertEpidemicCards() {
        int numPiles = NUM_EPIDEMIC_CARDS;
        int pileSize = playerDeck.size() / numPiles;

        List<Card> newPlayerDeck = new ArrayList<>();

        for (int i = 0; i < numPiles; i++) {
            List<Card> pile = new ArrayList<>(playerDeck.subList(i * pileSize, (i + 1) * pileSize));
            EpidemicCard epidemicCard = new EpidemicCard();
            pile.add(epidemicCard);
            Collections.shuffle(pile);
            newPlayerDeck.addAll(pile);
        }

        if (playerDeck.size() % numPiles != 0) {
            int remainingCardsIndex = numPiles * pileSize;
            newPlayerDeck.addAll(playerDeck.subList(remainingCardsIndex, playerDeck.size()));
        }

        playerDeck = newPlayerDeck;
    }

    public DeckManager copy(Board newBoard) {
        DeckManager newDeckManager = new DeckManager(newBoard);

        // Copy playerDeck
        List<Card> newPlayerDeck = new ArrayList<>();
        for (Card card : playerDeck) {
            newPlayerDeck.add(card.copy());
        }
        newDeckManager.playerDeck = newPlayerDeck;

        // Copy infectionDeck
        List<Card> newInfectionDeck = new ArrayList<>();
        for (Card card : infectionDeck) {
            newInfectionDeck.add(card.copy());
        }
        newDeckManager.infectionDeck = newInfectionDeck;

        // Copy playerDiscardPile
        List<Card> newPlayerDiscardPile = new ArrayList<>();
        for (Card card : playerDiscardPile) {
            newPlayerDiscardPile.add(card.copy());
        }
        newDeckManager.playerDiscardPile = newPlayerDiscardPile;

        // Copy infectionDiscardPile
        List<Card> newInfectionDiscardPile = new ArrayList<>();
        for (Card card : infectionDiscardPile) {
            newInfectionDiscardPile.add(card.copy());
        }
        newDeckManager.infectionDiscardPile = newInfectionDiscardPile;

        return newDeckManager;
    }

    //getters

    public List<Card> getPlayerDiscardPile() {
        return  playerDiscardPile;
    }

    public List<Card> getInfectionDiscardPile() {
        return  infectionDiscardPile;
    }

}