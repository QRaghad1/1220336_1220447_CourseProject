package com.example.a1220336_1220447_courseproject.api;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.a1220336_1220447_courseproject.models.Event;

public class ApiClient {

    private static final String API_URL = "https://mocki.io/v1/58c5bcd5-26e5-43bb-bbde-01a2b296bc8d";

    public interface ApiCallback {
        void onSuccess(List<Event> events);
        void onFailure(String error);
    }

    public static void fetchEvents(ApiCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                JSONArray array = new JSONArray(sb.toString());
                List<Event> events = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Event event = new Event(
                            obj.optInt("id"),
                            obj.optString("title"),
                            obj.optString("description"),
                            obj.optString("category"),
                            obj.optString("date"),
                            obj.optString("time"),
                            obj.optString("location"),
                            obj.optInt("seats"),
                            obj.optString("image")
                    );
                    events.add(event);
                }
                callback.onSuccess(events);

            } catch (Exception e) {
                callback.onFailure(e.getMessage());
            }
        }).start();
    }
}