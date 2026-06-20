package com.example.a1220336_1220447_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.adapters.UserAdapter;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.User;

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

        btnAddAdmin.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_fragment_container, new AddAdminFragment())
                        .addToBackStack(null)
                        .commit()
        );

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loadUsers() {
        users = dbHelper.getAllUsers();
        UserAdapter adapter = new UserAdapter(requireContext(), users, (user, position) -> {
            dbHelper.deleteUser(user.getId());
            Toast.makeText(getContext(), "User deleted!", Toast.LENGTH_SHORT).show();
            loadUsers();
        });
        listView.setAdapter(adapter);
    }
}