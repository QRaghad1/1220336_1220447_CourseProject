package com.example.a1220336_1220447_courseproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.api.ApiService;

public class IntroductionActivity extends AppCompatActivity {

    Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(v -> {
            connectButton.setEnabled(false);

            ApiService apiService = new ApiService(this);
            apiService.fetchAndStoreEvents(new ApiService.FetchCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        boolean rememberMe = prefs.getBoolean("rememberMe", false);
                        int userId = prefs.getInt("userId", -1);
                        boolean isAdmin = prefs.getBoolean("isAdmin", false);

                        Intent intent;
                        if (rememberMe && userId != -1) {
                            if (isAdmin) {
                                intent = new Intent(IntroductionActivity.this, AdminHomeActivity.class);
                            } else {
                                intent = new Intent(IntroductionActivity.this, MainActivity.class);
                            }
                        } else {
                            intent = new Intent(IntroductionActivity.this, LoginActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    });
                }

                @Override
                public void onFailure(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(IntroductionActivity.this,
                                "Connection failed. Please try again.", Toast.LENGTH_LONG).show();
                        connectButton.setEnabled(true);
                    });
                }
            });
        });
    }
}