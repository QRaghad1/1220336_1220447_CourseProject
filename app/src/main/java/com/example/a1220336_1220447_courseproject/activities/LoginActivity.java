package com.example.a1220336_1220447_courseproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a1220336_1220447_courseproject.R;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    CheckBox rememberMeCheckBox;
    Button loginButton, signUpButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // لو Remember Me كان مفعل قبل
        String savedEmail = sharedPreferences.getString("email", "");
        if (!savedEmail.isEmpty()) {
            emailEditText.setText(savedEmail);
            rememberMeCheckBox.setChecked(true);
        }

        loginButton.setOnClickListener(v -> loginUser());

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validation
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return;
        }

        // Remember Me
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMeCheckBox.isChecked()) {
            editor.putString("email", email);
        } else {
            editor.remove("email");
        }
        editor.apply();

        // مؤقتاً ننقل للـ Home - لاحقاً نربطها بالـ Database
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}