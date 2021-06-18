import java.io.*;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public class WeatherApi {

    static String getWeatherApiToken() {
        return "ba42e722-3ebb-4ebb-886e-42442273cc21";
    }

    static String getWeatherProviderUrl() {
        return "https://api.weather.yandex.ru/v2/informers";
    }

    static JSONObject getWeather(String lat, String lon) throws IOException {
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

}
