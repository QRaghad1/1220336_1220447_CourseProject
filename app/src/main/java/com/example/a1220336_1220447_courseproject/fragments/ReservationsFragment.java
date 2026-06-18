package com.example.a1220336_1220447_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.adapters.ReservationAdapter;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Reservation;

import java.util.List;

public class ReservationsFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ListView listView;
    private ReservationAdapter adapter;

    public ReservationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);

        listView = view.findViewById(R.id.listReservations);
        dbHelper = DatabaseHelper.getInstance(getContext());

        loadReservations();

        return view;
    }

    private void loadReservations() {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId != -1) {
            List<Reservation> reservations = dbHelper.getUserReservations(userId);
            if (reservations != null && !reservations.isEmpty()) {
                adapter = new ReservationAdapter(requireContext(), reservations);
                listView.setAdapter(adapter);
            } else {
                // You could show a "No reservations yet" text here
            }
        }
    }
}