package com.example.a1220336_1220447_courseproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a1220336_1220447_courseproject.R;

public class IntroductionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        Button connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}