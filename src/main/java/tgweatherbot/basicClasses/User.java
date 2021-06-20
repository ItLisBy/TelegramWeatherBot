package tgweatherbot.basicClasses;

public class User {
    private String name;
    private String chatID;
    private String city;

    public String getName() {
        return name;
    }

    public String getChatID() {
        return chatID;
    }

    public String getCity() {
        return city;
    }

    public User(String name, String chatID, String city) {
        this.name = name;
        this.chatID = chatID;
        this.city = city;
    }
}
