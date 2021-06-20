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

    public static String getWeatherApiToken() {
        return "ba42e722-3ebb-4ebb-886e-42442273cc21";
    }

    public static String getWeatherProviderUrl() {
        return "https://api.weather.yandex.ru/v2/informers";
    }

    public static JSONObject getWeather(String lat, String lon) throws IOException {
        JSONObject json_response = null;

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getWeatherProviderUrl() + String.format("?lat=%s&lon=%s", lat, lon))
                .addHeader("X-Yandex-API-Key", getWeatherApiToken())  // add request headers
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            //System.out.println(response.body().string());
            json_response = new JSONObject(response.body().string());
        }

        return json_response;
    }

    public static JSONObject getWeather(City city) throws IOException {
        return getWeather(city.getLat(), city.getLon());
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
        try {
            FileWriter json = new FileWriter("weather.json");
            json.write(json_response.toString());
            json.flush();
            json.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
