package tgweatherbot;

import com.vdurmont.emoji.EmojiParser;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgweatherbot.basicClasses.User;

import java.io.FileWriter;
import java.io.IOException;

public class WeatherBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage()) {
            if (update.getMessage().getText().startsWith("/")) {
                String command = update.getMessage().getText().substring(1);
                switch (command) {
                    case "weather": {
                        JSONObject weather = null;
                        try {
                             weather = WeatherApi.getWeather("55.75396", "37.620393");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try (FileWriter j_file = new FileWriter("j_test.json", false)){
                            j_file.write(weather.toString());
                            j_file.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sendShortWeather(weather, update.getMessage().getChatId().toString());
                        break;
                    }
                    case "w": {
                        WeatherApi.getWeatherWeb();
                        break;
                    }
                    case "start": {
                        break;
                    }
                    case "db": {
                        System.out.println("DB");
                        DB.addUser(new User("lis", update.getMessage().getChatId().toString(), "Miensk"));
                        break;
                    }

                    default: {
                        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
                        message.setChatId(update.getMessage().getChatId().toString());
                        message.enableMarkdown(true);
                        message.setText("`No such command.`\n ");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }

    @Override
    public String getBotUsername() {
        return "itlis_weather_bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TG_TOKEN");
    }

    public void sendShortWeather(JSONObject weather, String chatId) {
        SendMessage curr_message = new SendMessage(); // Create a SendMessage object with mandatory fields
        curr_message.setChatId(chatId);
        curr_message.enableMarkdown(true);
        curr_message.setText(String.format("City:%s %s\nCurrent temp: %d\nFeels like: %d",
                "Minsk",
                getWeatherEmoji(weather.getJSONObject("fact").getString("condition")),
                weather.getJSONObject("fact").getInt("temp"),
                weather.getJSONObject("fact").getInt("feels_like")));
        try {
            execute(curr_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getWeatherEmoji(String weather) {
        String emoji = ":cat:";
        if (weather.matches(".*snow.*")) {
            emoji = ":snowflake:";
        }
        else if (weather.matches("clear")) {
            emoji = ":sunny:";
        }
        else if (weather.matches("owercast")) {
            emoji = ":foggy:";
        }
        else if (weather.matches("thunderstorm.*")) {
            emoji = ":sunny:";
        }
        else if (weather.matches(".*cloudy")) {
            emoji = ":cloud:";
        }
        else if (weather.matches(".*rain") ||
                weather.matches("shower") ||
                weather.matches("drizzle")) {
            emoji = ":umbrella:";
        }
        return EmojiParser.parseToUnicode(emoji);
    }
}
