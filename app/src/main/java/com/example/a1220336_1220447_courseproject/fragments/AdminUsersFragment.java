package com.example.a1220336_1220447_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.User;

import java.util.List;
import java.util.ArrayList;

public class AdminUsersFragment extends Fragment {

    ListView listView;
    DatabaseHelper dbHelper;
    List<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);

        listView = view.findViewById(R.id.listUsers);
        dbHelper = new DatabaseHelper(getContext());

        loadUsers();

        listView.setOnItemLongClickListener((parent, v, position, id) -> {
            User user = users.get(position);
            dbHelper.deleteUser(user.getId());
            Toast.makeText(getContext(), "User deleted!", Toast.LENGTH_SHORT).show();
            loadUsers();
            return true;
        });

        return view;
    }

    private void loadUsers() {
        users = dbHelper.getAllUsers();
        List<String> displayList = new ArrayList<>();
        for (User u : users) {
            displayList.add(u.getFirstName() + " " + u.getLastName() + "\n" + u.getEmail());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);
    }
}