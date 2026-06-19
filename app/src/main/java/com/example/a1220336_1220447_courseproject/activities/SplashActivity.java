package com.example.a1220336_1220447_courseproject.activities;

import android.content.Intent;
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
            startActivity(new Intent(SplashActivity.this, IntroductionActivity.class));
            finish();
        }, 3000);
    }
}