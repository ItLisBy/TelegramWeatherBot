package tgweatherbot.basicClasses;

public class City{
    private String lat;
    private String lon;
    private String name;

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public City(String name, String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }
}