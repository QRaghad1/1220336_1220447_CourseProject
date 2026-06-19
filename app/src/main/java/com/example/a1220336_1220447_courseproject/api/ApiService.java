package com.example.a1220336_1220447_courseproject.api;

import android.content.Context;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;
import java.util.List;

public class ApiService {

    private DatabaseHelper dbHelper;

    public ApiService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public interface FetchCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public void fetchAndStoreEvents(FetchCallback callback) {
        ApiClient.fetchEvents(new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(List<Event> events) {
                dbHelper.deleteAllEvents();  // امسح القديم
                for (Event event : events) {
                    dbHelper.addEvent(event);  // احفظ الجديد
                }
                callback.onSuccess();
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }
}