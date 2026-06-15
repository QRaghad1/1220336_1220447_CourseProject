package com.example.a1220336_1220447_courseproject.fragments;

import android.app.Activity;
import android.content.Intent;
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

public class ProfileFragment extends Fragment {

    ImageView imgProfile;
    Button btnChangePhoto, btnSave;
    EditText etFirstName, etLastName, etPhone, etPassword;

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

        // Image picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        imgProfile.setImageURI(imageUri);
                    }
                });

        btnChangePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validation
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

            Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}