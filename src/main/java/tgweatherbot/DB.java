package tgweatherbot;

import tgweatherbot.basicClasses.City;
import tgweatherbot.basicClasses.User;

import java.sql.*;
import java.util.ArrayList;

public class DB {

    public static Connection connect() throws SQLException {
        Connection connection = DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"));
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (name text, city text, chatid text unique)");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS cities (name text unique, lat text, lon text )");

        return connection;
    }

    public static City getCity(String city) {
        City r_city = null;
        Connection connection = null;
        try {
            connection = connect();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM cities WHERE name = '%s'", city.toLowerCase()));
            rs.next();
            r_city = new City(rs.getString("name"),
                    rs.getString("lat"),
                    rs.getString("lon"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return r_city;
    }

    public static ArrayList<City> getAllCities() {
        ArrayList<City> cities = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connect();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT * FROM cities");
            while (rs.next()){
                cities.add(new City(rs.getString("name"),
                        rs.getString("lat"),
                        rs.getString("lon")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return cities;
    }

    public static User getUser(String chatID) {
        User user = null;
        Connection connection = null;
        try {
            connection = connect();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM users WHERE chatID = '%s'", chatID));
            rs.next();
            user = new User(rs.getString("name"),
                    rs.getString("lat"),
                    rs.getString("lon"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return user;
    }

    public static ArrayList<User> getAllUsers() {
        ArrayList<User> user = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connect();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT * FROM users");
            while (rs.next()){
                user.add(new User(rs.getString("name"),
                        rs.getString("chatID"),
                        rs.getString("city")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return user;
    }

    public static void addUser(String name, String chatID, String city) {
        Connection connection = null;
        try {
            connection = connect();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> IDs = new ArrayList<>();
            ResultSet rs = statement.executeQuery("SELECT name, chatID FROM users");
            while (rs.next()) {
                names.add(rs.getString("name"));
                IDs.add(rs.getString("chatID"));
            }

            if (IDs.contains(chatID)){
                if (!names.contains(name)) {
                    statement.executeUpdate(String.format("UPDATE users SET name='%s' WHERE chatID='%s'", name, chatID));
                }
            }
            else {
                statement.executeUpdate(String.format("INSERT INTO users (name, chatID, city) VALUES ('%s', '%s', '%s')", name, chatID, city.toLowerCase()));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void addUser(User user) {
        addUser(user.getName(), user.getChatID(), user.getCity());
    }

    public static void addUser(String name, String chatID) {
        addUser(name, chatID, "Miensk");
    }
}
