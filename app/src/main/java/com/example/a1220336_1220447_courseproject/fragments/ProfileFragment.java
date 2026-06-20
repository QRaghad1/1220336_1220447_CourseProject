package com.example.a1220336_1220447_courseproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.User;

public class ProfileFragment extends Fragment {

    ImageView imgProfile;
    Button btnChangePhoto, btnSave;
    EditText etFirstName, etLastName, etPhone, etPassword;
    DatabaseHelper dbHelper;
    User currentUser;
    String selectedImageUri = null;

    ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfile = view.findViewById(R.id.imgProfile);
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto);
        btnSave = view.findViewById(R.id.btnSave);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPhone = view.findViewById(R.id.etPhone);
        etPassword = view.findViewById(R.id.etPassword);

        dbHelper = DatabaseHelper.getInstance(getContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Session error. Please login again.", Toast.LENGTH_SHORT).show();
            return view;
        }

        currentUser = dbHelper.getUserById(userId);

        if (currentUser != null) {
            etFirstName.setText(currentUser.getFirstName());
            etLastName.setText(currentUser.getLastName());
            etPhone.setText(currentUser.getPhone());
            loadProfileImage(currentUser.getProfileImage());
        }

        // Image picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        // Take persistent permission so URI stays valid after restart
                        requireContext().getContentResolver().takePersistableUriPermission(
                                imageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        selectedImageUri = imageUri.toString();
                        imgProfile.setImageURI(imageUri);
                    }
                });

        btnChangePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (firstName.length() < 3) {
                etFirstName.setError("Minimum 3 characters");
                return;
            }
            if (lastName.length() < 3) {
                etLastName.setError("Minimum 3 characters");
                return;
            }
            if (phone.isEmpty()) {
                etPhone.setError("Required");
                return;
            }
            if (!password.isEmpty()) {
                if (password.length() < 6 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
                    etPassword.setError("Min 6 chars, 1 letter, 1 number");
                    return;
                }
            }

            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setPhone(phone);
            if (!password.isEmpty()) {
                currentUser.setPassword(hashPassword(password));
            }
            if (selectedImageUri != null) {
                currentUser.setProfileImage(selectedImageUri);
            }

            boolean updated = dbHelper.updateUser(currentUser);
            if (updated) {
                Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Update failed!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadProfileImage(String uriString) {
        if (uriString == null || uriString.isEmpty()) {
            imgProfile.setImageResource(R.mipmap.ic_launcher);
            return;
        }
        try {
            Uri uri = Uri.parse(uriString);
            imgProfile.setImageURI(null); // force refresh
            imgProfile.setImageURI(uri);
        } catch (Exception e) {
            imgProfile.setImageResource(R.mipmap.ic_launcher);
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