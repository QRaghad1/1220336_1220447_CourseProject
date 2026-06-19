package com.example.a1220336_1220447_courseproject.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersFragment extends Fragment {

    ListView listView;
    DatabaseHelper dbHelper;
    List<User> users;
    Button btnAddAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);

        listView = view.findViewById(R.id.listUsers);
        btnAddAdmin = view.findViewById(R.id.btnAddAdmin);
        dbHelper = DatabaseHelper.getInstance(requireContext());

        loadUsers();

        // Delete user on long click
        listView.setOnItemLongClickListener((parent, v, position, id) -> {
            User user = users.get(position);
            dbHelper.deleteUser(user.getId());
            Toast.makeText(getContext(), "User deleted!", Toast.LENGTH_SHORT).show();
            loadUsers();
            return true;
        });

        // Add new admin
        btnAddAdmin.setOnClickListener(v -> showAddAdminDialog());

        return view;
    }

    private void showAddAdminDialog() {
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_add_admin, null);

        EditText etEmail = dialogView.findViewById(R.id.etAdminEmail);
        EditText etPassword = dialogView.findViewById(R.id.etAdminPassword);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add New Admin")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(requireContext(), "All fields required!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(requireContext(), "Invalid email!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.length() < 6) {
                        Toast.makeText(requireContext(), "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    User admin = new User(0, "Admin", "User", email, password, "", "", "", "", true);
                    long result = dbHelper.addUser(admin);

                    if (result != -1) {
                        Toast.makeText(requireContext(), "Admin added successfully!", Toast.LENGTH_SHORT).show();
                        loadUsers();
                    } else {
                        Toast.makeText(requireContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadUsers() {
        users = dbHelper.getAllUsers();
        List<String> displayList = new ArrayList<>();
        for (User u : users) {
            displayList.add(u.getFirstName() + " " + u.getLastName() + "\n" + u.getEmail());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);
    }
}