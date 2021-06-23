package tgweatherbot;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import tgweatherbot.basicClasses.City;

public class WeatherApi {

    static JSONObject all_weather = new JSONObject();

    public static String getWeatherApiToken() {
        return System.getenv("WEATHER_TOKEN");
    }

    public static String getWeatherProviderUrl() {
        return "https://api.weather.yandex.ru/v2/informers";
    }

    public static JSONObject getWeather(City city) {
        return all_weather.getJSONObject(city.getName());
    }

    public static void getWeatherWeb() {
        JSONObject json_response = new JSONObject();
        OkHttpClient httpClient = new OkHttpClient();
        Request request = null;
        for (var city : DB.getAllCities()) {
            request = new Request.Builder()
                    .url(getWeatherProviderUrl() + String.format("?lat=%s&lon=%s", city.getLat(), city.getLon()))
                    .addHeader("X-Yandex-API-Key", getWeatherApiToken())  // add request headers
                    .build();
            try (Response response = httpClient.newCall(request).execute()) {

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                json_response.put(city.getName(), new JSONObject(response.body().string()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        all_weather = json_response;
    }

    public class ScheduleRequests {
        Timer timer;

        public ScheduleRequests(int seconds) {
            timer = new Timer();
            timer.schedule(new Schedule(), seconds * 1000L);
        }

        class Schedule extends TimerTask {
            public void run() {
                getWeatherWeb();
            }
        }
    }

}
