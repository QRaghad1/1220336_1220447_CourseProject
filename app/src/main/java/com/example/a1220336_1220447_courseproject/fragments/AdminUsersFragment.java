package com.example.a1220336_1220447_courseproject.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.User;

import java.util.List;

public class AdminUsersFragment extends Fragment {

    ListView listView;
    DatabaseHelper dbHelper;
    List<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);

        listView = view.findViewById(R.id.listUsers);
        Button btnAddAdmin = view.findViewById(R.id.btnAddAdmin);
        dbHelper = DatabaseHelper.getInstance(getContext());

        loadUsers();

        btnAddAdmin.setOnClickListener(v -> {
            AddAdminFragment addAdminFragment = new AddAdminFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.admin_fragment_container, addAdminFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dbHelper != null) {
            loadUsers();
        }
    }

    private void loadUsers() {
        users = dbHelper.getAllUsers();

        ArrayAdapter<User> adapter = new ArrayAdapter<User>(requireContext(),
                R.layout.item_user, users) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext())
                            .inflate(R.layout.item_user, parent, false);
                }

                User user = users.get(position);

                TextView tvName = convertView.findViewById(R.id.tvUserName);
                TextView tvEmail = convertView.findViewById(R.id.tvUserEmail);
                TextView tvRole = convertView.findViewById(R.id.tvUserRole);
                Button btnDelete = convertView.findViewById(R.id.btnDeleteUser);

                tvName.setText(user.getFirstName() + " " + user.getLastName());
                tvEmail.setText(user.getEmail());
                tvRole.setText(user.isAdmin() ? "Admin" : "User");

                if (user.isAdmin()) {
                    btnDelete.setVisibility(View.GONE); // can't delete admin
                } else {
                    btnDelete.setVisibility(View.VISIBLE);
                    btnDelete.setOnClickListener(v -> {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Delete User")
                                .setMessage("Are you sure you want to delete " + user.getFirstName() + "?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    dbHelper.deleteUser(user.getId());
                                    Toast.makeText(requireContext(), "User deleted!", Toast.LENGTH_SHORT).show();
                                    loadUsers();
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    });
                }

                return convertView;
            }
        };

        listView.setAdapter(adapter);
    }
}