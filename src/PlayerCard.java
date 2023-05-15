
public class PlayerCard extends Card {
    private City city;

    public PlayerCard(int id, String name, String description, City city) {
        super(id, name, description);
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


    @Override
    public String toString() {
        return     city.getColor() + " " +
                city.getName();
    }

    @Override
    public PlayerCard copy() {
        return new PlayerCard(getCityId(),getName(),getColor(), city);
    }

}