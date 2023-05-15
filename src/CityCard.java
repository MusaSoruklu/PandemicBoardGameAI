/**
 * CityCard class represents a city card in the Pandemic game.
 * It extends the abstract Card class and contains additional information
 * about the associated city.
 */
public class CityCard extends Card {
    private City city;

    /**
     * Constructor for CityCard.
     *
     * @param id The card's unique identifier.
     * @param name The name of the city on the card.
     * @param color The color of the card, representing the disease color.
     * @param city The City object associated with the card.
     */
    public CityCard(int id, String name, String color, City city) {
        super(id, name, color);
        this.city = city;
    }

    /**
     * Sets the city associated with this card.
     *
     * @param city The City object to be associated with the card.
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * Returns the city associated with this card.
     *
     * @return The City object associated with the card.
     */
    public City getCity() {
        return city;
    }

    /**
     * Creates a copy of this CityCard.
     *
     * @return A new CityCard object with the same properties as this card.
     */
    @Override
    public CityCard copy() {
        return new CityCard(getCityId(), getName(), getColor(), city);
    }
}
