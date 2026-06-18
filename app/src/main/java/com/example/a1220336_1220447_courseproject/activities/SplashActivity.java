package com.example.a1220336_1220447_courseproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a1220336_1220447_courseproject.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView logoText = findViewById(R.id.logoText);
        TextView appName = findViewById(R.id.appName);

        Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        logoText.startAnimation(scaleAnim);
        appName.startAnimation(fadeAnim);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            boolean rememberMe = prefs.getBoolean("rememberMe", false);
            int userId = prefs.getInt("userId", -1);
            boolean isAdmin = prefs.getBoolean("isAdmin", false);

            Intent intent;
            if (rememberMe && userId != -1) {
                if (isAdmin) {
                    intent = new Intent(SplashActivity.this, AdminHomeActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
            } else {
                intent = new Intent(SplashActivity.this, IntroductionActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }
}