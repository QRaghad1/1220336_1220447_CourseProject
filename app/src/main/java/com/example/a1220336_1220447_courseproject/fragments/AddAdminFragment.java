package com.example.a1220336_1220447_courseproject.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.User;

public class AddAdminFragment extends Fragment {

    EditText etFirstName, etLastName, etEmail, etPassword;
    Button btnSaveAdmin, btnCancelAddAdmin;
    DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);

        dbHelper = DatabaseHelper.getInstance(getContext());

        etFirstName = view.findViewById(R.id.etAdminFirstName);
        etLastName = view.findViewById(R.id.etAdminLastName);
        etEmail = view.findViewById(R.id.etAdminEmail);
        etPassword = view.findViewById(R.id.etAdminPassword);
        btnSaveAdmin = view.findViewById(R.id.btnSaveAdmin);
        btnCancelAddAdmin = view.findViewById(R.id.btnCancelAddAdmin);

        btnSaveAdmin.setOnClickListener(v -> saveAdmin());
        btnCancelAddAdmin.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void saveAdmin() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (firstName.isEmpty() || firstName.length() < 3) {
            etFirstName.setError("First name must be at least 3 characters");
            return;
        }
        if (lastName.isEmpty() || lastName.length() < 3) {
            etLastName.setError("Last name must be at least 3 characters");
            return;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email format");
            return;
        }
        if (!isValidPassword(password)) {
            etPassword.setError("Min 6 chars, at least 1 letter and 1 number");
            return;
        }

        User admin = new User(0, firstName, lastName, email,
                hashPassword(password), "Male", "Admin", "", null, true);

        long result = dbHelper.addUser(admin);
        if (result != -1) {
            Toast.makeText(getContext(), "Admin added!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 6) return false;
        boolean hasLetter = false, hasNumber = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasNumber = true;
        }
        return hasLetter && hasNumber;
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
