package com.example.a1220336_1220447_courseproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.api.ApiService;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        Button connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(v -> {
            connectButton.setEnabled(false);

            ApiService apiService = new ApiService(this);
            apiService.fetchAndStoreEvents(new ApiService.FetchCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        startActivity(new Intent(IntroductionActivity.this, LoginActivity.class));
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