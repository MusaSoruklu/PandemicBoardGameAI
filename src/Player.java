import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    public static final int MAX_HAND_SIZE = 7;
    private String name;
    private City currentCity;
    private int numActions;
    private List<Card> hand;
    private final Role role;
    protected IntelligentAgent agent;
    private final Set<City> availableCities;
    private final Set<City> currentCityHand;

    public Player(String name, City startingCity, Role role, IntelligentAgent agent) {
        this.name = name;
        this.currentCity = startingCity;
        this.numActions = 4;
        this.hand = new ArrayList<>();
        this.role = role;
        this.agent = agent;
        availableCities = new HashSet<>();
        currentCityHand = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City city) {
        currentCity = city;
        updateCurrentCityHand();
    }

    public int getNumActions() {
        return numActions;
    }

    public void setNumActions(int numActions) {
        this.numActions = numActions;
    }

    public int getCardsTowardsCure() {
        Map<String, Integer> colorCountMap = new HashMap<>();
        for (Card card : hand) {
            if (card instanceof PlayerCard) {
                String color = ((PlayerCard) card).getCity().getColor();
                colorCountMap.put(color, colorCountMap.getOrDefault(color, 0) + 1);
            }
        }

        int cardsTowardsCure = 0;
        for (int cardCount : colorCountMap.values()) {
            cardsTowardsCure += Math.max(0, cardCount - 3); // Assuming 4 cards are needed for a cure
        }
        return cardsTowardsCure;
    }

    public List<Card> getHand() {
        return hand;
    }

    protected void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
        if (card instanceof PlayerCard) {
            PlayerCard playerCard = (PlayerCard) card;
            if (!playerCard.getCity().equals(currentCity)) {
                availableCities.add(playerCard.getCity());
            } else {
                currentCityHand.add(playerCard.getCity());
            }
        }
    }

    public List<Action> getAvailableActions(Board board) {
        List<Action> actions = new ArrayList<>();

        // Add available actions based on player's current state

        // Drive/Ferry: Add neighboring cities as possible destinations
        for (City neighbor : this.getCurrentCity().getNeighbors()) {
            actions.add(new DriveFerryAction(neighbor));
        }

        // Direct Flight: Add available cities as destinations if the player has the corresponding city card
        for (City city : availableCities) {
            PlayerCard playerCard = (PlayerCard) findCardInHand(city.getName());
            if (playerCard != null) {
                actions.add(new DirectFlightAction(playerCard));
            }

            // Charter Flight: Add available cities as destinations if the player has the current city card
            if (currentCityHand.contains(currentCity)) {
                actions.add(new CharterFlightAction(city));
            }
        }

        // Count the number of research stations on the board
        for (City city : board.getCities()) {
            // Shuttle Flight: Add cities with research stations as destinations if the current city has a research station
            if (!city.getName().equals(this.getCurrentCity().getName()) && city.hasResearchStation() && this.getCurrentCity().hasResearchStation()) {
                actions.add(new ShuttleFlightAction(city, currentCity));
            }
        }

        // Build Research Station: Add action if the current city does not have a research station and the player is an Operations Expert or has the current city card
        if (!this.getCurrentCity().hasResearchStation() && this.getRole() instanceof OperationsExpert && board.getNumResearchStations() < 6) {
            actions.add(new BuildResearchStationAction(currentCity));
        } else {
            if (!this.getCurrentCity().hasResearchStation() && board.getNumResearchStations() < 6) {
                for (Card card : this.getHand()) {
                    if (card instanceof PlayerCard && ((PlayerCard) card).getCity().getId() == this.getCurrentCity().getId()) {
                        actions.add(new BuildResearchStationAction(currentCity));
                    }
                }
            }
        }

        // Treat Disease: Add action for each color with disease cubes in the current city
        if (currentCity.getNumDiseaseCubes(currentCity.getColor()) > 0) {
            for (String color : Disease.COLORS) {
                int numCubes = this.getCurrentCity().getNumDiseaseCubes(color);
                if (numCubes > 0) {
                    actions.add(new TreatDiseaseAction(color, board, currentCity));
                }
            }
        }

        // Cure Disease: Add action if the current city has a research station and the player can discover a cure
        if (this.currentCity.hasResearchStation()) {
            for (String color : Disease.COLORS) {
                if (canDiscoverCure(color) && !board.getDisease(color).isCured()) {
                    List<Card> cardsToDiscard = new ArrayList<>();
                    for (Card card : hand) {
                        if (card.getColor().equals(color)) {
                            cardsToDiscard.add(card);
                        }
                    }
                    if (cardsToDiscard.size() >= 5) {
                        actions.add(new CureDiseaseAction(color, cardsToDiscard));
                    }
                }
            }
        }

        // Share Knowledge: Add actions to give or take city cards with other players in the same city
        City currentCity = this.getCurrentCity();
        for (Player otherPlayer : board.getPlayers()) {
            if (!otherPlayer.equals(this) && otherPlayer.getCurrentCity().equals(currentCity)) {
                // Give the City card that matches the city you are in to another player
                for (Card card : this.getHand()) {
                    if (card instanceof PlayerCard && ((PlayerCard) card).getCity().equals(currentCity)) {
                        actions.add(new ShareKnowledgeAction(this, otherPlayer, (PlayerCard) card));
                    }
                }

                // Take the City card that matches the city you are in from another player
                for (Card card : otherPlayer.getHand()) {
                    if (card instanceof PlayerCard && ((PlayerCard) card).getCity().equals(currentCity)) {
                        actions.add(new ShareKnowledgeAction(otherPlayer, this, (PlayerCard) card));
                    }
                }
            }
        }

// Add option to pass (i.e., do nothing) if no other actions are available
        actions.add(new PassAction());

        return actions;
    }

    //checks if the player is able to discover a cure by checking if the user has 5 cards of the same color
    public boolean canDiscoverCure(String color) {
        int count = 0;
        for (Card card : hand) {
            if (card instanceof PlayerCard && ((PlayerCard) card).getCity().getColor().equals(color)) {
                count++;
                if (count == 5) {
                    return true;
                }
            }
        }
        return false;
    }

    private Map<Card, Double> getCardUsefulness(Board board, DeckManager deckManager) {
        // Get the list of all other players
        List<Player> otherPlayers = board.getPlayers().stream()
                .filter(p -> !p.equals(this))
                .collect(Collectors.toList());

        // Calculate the probability of drawing each card based on the discard piles
        Map<Card, Double> cardDrawProbabilities = new HashMap<>();
        for (Card card : deckManager.getPlayerDiscardPile()) {
            double probability = 1.0 / (deckManager.getPlayerDeck().size() + deckManager.getPlayerDiscardPile().size());
            cardDrawProbabilities.put(card, probability);
        }

        // Check which cards the other players have in their hands
        Map<PlayerCard, Integer> cardsInOtherHands = new HashMap<>();
        for (Player player : otherPlayers) {
            for (Card card : player.getHand()) {
                if (card instanceof PlayerCard) {
                    PlayerCard playerCard = (PlayerCard) card;
                    cardsInOtherHands.put(playerCard, cardsInOtherHands.getOrDefault(playerCard, 0) + 1);
                }
            }
        }

        // Evaluate the usefulness of each card in the player's hand
        Map<Card, Double> cardUsefulness = new HashMap<>();
        for (Card card : this.getHand()) {
            if (card instanceof PlayerCard) {
                PlayerCard playerCard = (PlayerCard) card;
                double usefulness = cardDrawProbabilities.getOrDefault(playerCard, 0.0) + cardsInOtherHands.getOrDefault(playerCard, 0);
                cardUsefulness.put(card, usefulness);
            } else {
                cardUsefulness.put(card, Double.MAX_VALUE); // Keep event cards and other non-city cards
            }
        }

        return cardUsefulness;
    }

    public void discardExcessCards(Board board, DeckManager deckManager) {
        if (this.getHand().size() <= 7) {
            return;
        }

        while (this.getHand().size() > 7) {
            Map<Card, Double> cardUsefulness = getCardUsefulness(board, deckManager);

            if (this.name.equals("AI")) {
                // AI player discarding logic
                // ...

                // Find the least useful card
                Card leastUsefulCard = this.getHand().stream()
                        .min(Comparator.comparing(cardUsefulness::get))
                        .orElse(null);

                // Discard the least useful card
                if (leastUsefulCard != null) {
                    this.getHand().remove(leastUsefulCard);
                    System.out.println("AI is discarding: " + leastUsefulCard.getName());
                    deckManager.discardPlayerCard(leastUsefulCard);
                }
            } else {
                // Human player discarding logic
                // ...

                // Find the least useful card
                this.getHand().stream()
                        .min(Comparator.comparing(cardUsefulness::get)).ifPresent(leastUsefulCard -> System.out.println("I recommend you discard the " + leastUsefulCard.getName() + " card as it is the least useful based on the current game state."));

                Scanner scanner = new Scanner(System.in);
                System.out.println(name + ", your hand size (" + hand.size() + ") exceeds the maximum limit. Please discard " + (hand.size() - MAX_HAND_SIZE) + " card/s.");
                System.out.println("Current hand: " + handToString());
                System.out.print("Enter the name of the card you want to discard: ");
                String cardName = scanner.nextLine().trim();

                Card cardToDiscard = findCardInHand(cardName);
                if (cardToDiscard != null) {
                    hand.remove(cardToDiscard);
                    deckManager.discardPlayerCard(cardToDiscard);
                    System.out.println("You have discarded the " + cardName + " card.");
                } else {
                    System.out.println("Card not found in your hand. Please try again.");
                }
            }
        }
    }

    public Card findCardInHand(String cardName) {
        for (Card card : hand) {
            if (card.getName().equalsIgnoreCase(cardName)) {
                return card;
            }
        }
        return null;
    }

    private String handToString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append("(").append(card.getColor()).append(") ").append(card.getName()).append(", ");
        }
        return sb.toString();
    }

    public void removeCardFromHand(Card card) {
        if (hand.remove(card) && card instanceof PlayerCard) {
            PlayerCard playerCard = (PlayerCard) card;
            City cardCity = playerCard.getCity();
            if (cardCity.equals(currentCity)) {
                currentCityHand.remove(cardCity);
            }
            availableCities.remove(cardCity);
        }
    }

    //this is used for speeding up the getAvailableActions method
    public void updateCurrentCityHand() {
        currentCityHand.clear();
        for (Card card : hand) {
            if (card instanceof PlayerCard) {
                PlayerCard playerCard = (PlayerCard) card;
                City cardCity = playerCard.getCity();
                if (cardCity.equals(currentCity)) {
                    currentCityHand.add(cardCity);
                }
            }
        }
    }

    public Role getRole() {
        return role;
    }

    public Player copy(Board newBoard) {
        City newCurrentCity = null;
        for (City city : newBoard.getCities()) {
            if (city.getName().equals(this.currentCity.getName())) {
                newCurrentCity = city;
                break;
            }
        }

        // Create a deep copy of the hand
        List<Card> newHand = new ArrayList<>();
        for (Card card : this.getHand()) {
            newHand.add(card.copy());
        }

        Player newPlayer;
        if (name.equals("AI")) {
            newPlayer = new Player(name, newCurrentCity, role, new HeuristicIntelligentAgent());
        } else {
            newPlayer = new Player(name, newCurrentCity, role, null);
        }
        newPlayer.setNumActions(numActions);
        newPlayer.setHand(newHand);
        return newPlayer;


    }
}
