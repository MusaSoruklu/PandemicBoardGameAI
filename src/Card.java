public abstract class Card {
    private final int cityId;
    private final String name;
    private final String color;

    public Card(int cityId, String name, String color) {
        this.cityId = cityId;
        this.name = name;
        this.color = color;
    }

    public int getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public abstract Card copy();


}