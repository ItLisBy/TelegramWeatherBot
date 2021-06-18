import com.vdurmont.emoji.EmojiParser;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
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
                        sendWeather(weather, update.getMessage().getChatId().toString());
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
        return "1664450075:AAHvnffYMXdUuK_O79sRdLSRqrYQNBvg5Mw";
    }

    public void sendWeather(JSONObject weather, String chatId) {
        SendMessage curr_message = new SendMessage(); // Create a SendMessage object with mandatory fields
        curr_message.setChatId(chatId);
        curr_message.enableMarkdown(true);
        curr_message.setText(String.format("%s\nCurrent temp: %d\nFeels like: %d",
                EmojiParser.parseToUnicode(":cat:"),
                weather.getJSONObject("fact").getInt("temp"),
                weather.getJSONObject("fact").getInt("feels_like")));
        try {
            execute(curr_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getWeatherEmoji(String weather) {
        //TODO convert name of emoji to unicode
        return null;
    }
}
