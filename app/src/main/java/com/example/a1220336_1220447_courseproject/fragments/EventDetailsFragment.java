package com.example.a1220336_1220447_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;

public class EventDetailsFragment extends Fragment {

    private Event event;
    private DatabaseHelper dbHelper;

    public EventDetailsFragment(Event event) {
        this.event = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        TextView tvTitle = view.findViewById(R.id.tvDetailTitle);
        TextView tvCategory = view.findViewById(R.id.tvDetailCategory);
        TextView tvDate = view.findViewById(R.id.tvDetailDate);
        TextView tvTime = view.findViewById(R.id.tvDetailTime);
        TextView tvLocation = view.findViewById(R.id.tvDetailLocation);
        TextView tvSeats = view.findViewById(R.id.tvDetailSeats);
        TextView tvDescription = view.findViewById(R.id.tvDetailDescription);
        Button btnReserve = view.findViewById(R.id.btnReserve);
        Button btnFavorite = view.findViewById(R.id.btnFavorite);

        dbHelper = new DatabaseHelper(getContext());

        tvTitle.setText(event.getTitle());
        tvCategory.setText(event.getCategory());
        tvDate.setText(event.getDate());
        tvTime.setText(event.getTime());
        tvLocation.setText(event.getLocation());
        tvSeats.setText("Available Seats: " + event.getSeats());
        tvDescription.setText(event.getDescription());

        btnFavorite.setOnClickListener(v -> {
            dbHelper.addFavorite(1, event.getId());
            Toast.makeText(getContext(), "Added to Favorites!", Toast.LENGTH_SHORT).show();
        });

        btnReserve.setOnClickListener(v -> {
            ReservationFragment reservationFragment = new ReservationFragment(event);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, reservationFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}