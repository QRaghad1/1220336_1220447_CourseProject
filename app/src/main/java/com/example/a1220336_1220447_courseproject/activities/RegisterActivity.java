package com.example.a1220336_1220447_courseproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.User;

public class RegisterActivity extends AppCompatActivity {

    EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    Spinner genderSpinner, majorSpinner;
    Button registerButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = DatabaseHelper.getInstance(this);

        firstNameEditText       = findViewById(R.id.firstNameEditText);
        lastNameEditText        = findViewById(R.id.lastNameEditText);
        emailEditText           = findViewById(R.id.emailEditText);
        phoneEditText           = findViewById(R.id.phoneEditText);
        passwordEditText        = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        genderSpinner           = findViewById(R.id.genderSpinner);
        majorSpinner            = findViewById(R.id.majorSpinner);
        registerButton          = findViewById(R.id.registerButton);

        // Gender Spinner
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Select Gender", "Male", "Female"});
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Major Spinner
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Select Major", "Computer Engineering",
                        "Electrical Engineering", "Computer Science",
                        "Information Technology", "Other"});
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        majorSpinner.setAdapter(majorAdapter);

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String firstName       = firstNameEditText.getText().toString().trim();
        String lastName        = lastNameEditText.getText().toString().trim();
        String email           = emailEditText.getText().toString().trim();
        String phone           = phoneEditText.getText().toString().trim();
        String password        = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String gender          = genderSpinner.getSelectedItem().toString();
        String major           = majorSpinner.getSelectedItem().toString();

        // Validation
        if (firstName.length() < 3) {
            firstNameEditText.setError("Minimum 3 characters");
            return;
        }
        if (lastName.length() < 3) {
            lastNameEditText.setError("Minimum 3 characters");
            return;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return;
        }
        if (phone.isEmpty()) {
            phoneEditText.setError("Phone number is required");
            return;
        }
        if (gender.equals("Select Gender")) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if (major.equals("Select Major")) {
            Toast.makeText(this, "Please select major", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6 ||
                !password.matches(".*[a-zA-Z].*") ||
                !password.matches(".*[0-9].*")) {
            passwordEditText.setError("Min 6 chars, must contain letter and number");
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        // Hash password before saving
        String hashedPassword = hashPassword(password);
        User user = new User(0, firstName, lastName, email, hashedPassword, gender, major, phone, null, false);
        long result = databaseHelper.addUser(user);

        if (result != -1) {
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Email already exists!", Toast.LENGTH_SHORT).show();
        }
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
