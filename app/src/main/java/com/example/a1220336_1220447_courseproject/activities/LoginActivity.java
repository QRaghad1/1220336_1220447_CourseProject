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
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.User;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    CheckBox rememberMeCheckBox;
    Button loginButton, signUpButton;
    SharedPreferences sharedPreferences;
    DatabaseHelper dbHelper;

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
        dbHelper = DatabaseHelper.getInstance(this);

        // Auto-login if Remember Me was checked
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        int savedUserId = sharedPreferences.getInt("userId", -1);

        if (rememberMe && savedUserId != -1) {
            boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);
            if (isAdmin) {
                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            finish();
            return;
        }

        // Fill email if Remember Me was checked before
        String savedEmail = sharedPreferences.getString("email", "");
        if (!savedEmail.isEmpty()) {
            emailEditText.setText(savedEmail);
            rememberMeCheckBox.setChecked(true);
        }

        loginButton.setOnClickListener(v -> loginUser());

        signUpButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

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

        User user = dbHelper.getUserByEmail(email);

        if (user == null) {
            Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedInput = hashPassword(password);
        if (!hashedInput.equals(user.getPassword())) {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMeCheckBox.isChecked()) {
            editor.putString("email", email);
            editor.putString("password", hashPassword(password));
            editor.putBoolean("rememberMe", true);
        } else {
            editor.remove("email");
            editor.remove("password");
            editor.putBoolean("rememberMe", false);
        }

        editor.putInt("userId", user.getId());
        editor.putBoolean("isAdmin", user.isAdmin());
        editor.apply();

        Toast.makeText(this, "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT).show();

        if (user.isAdmin()) {
            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        finish();
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}