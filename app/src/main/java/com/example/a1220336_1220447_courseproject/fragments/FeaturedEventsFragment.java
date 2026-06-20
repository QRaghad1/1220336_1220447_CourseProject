package com.example.a1220336_1220447_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.adapters.EventAdapter;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;

import java.util.ArrayList;
import java.util.List;

public class FeaturedEventsFragment extends Fragment {

    RecyclerView recyclerView;
    EventAdapter adapter;
    DatabaseHelper dbHelper;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFeaturedEvents);
        dbHelper = DatabaseHelper.getInstance(getContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        List<Event> allEvents = dbHelper.getAllEvents();
        List<Event> featured = new ArrayList<>();

        // Featured = events with seats > 50 (more robust than hardcoded category)
        for (Event e : allEvents) {
            if (e.getSeats() > 50) {
                featured.add(e);
            }
        }

        // Fallback: if nothing matches, show all events
        if (featured.isEmpty()) {
            featured.addAll(allEvents);
        }

        adapter = new EventAdapter(getContext(), featured, new EventAdapter.OnEventClickListener() {
            @Override
            public void onEventClick(Event event) {
                EventDetailsFragment details = new EventDetailsFragment(event);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, details)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onFavoriteClick(Event event) {
                if (dbHelper.isFavorite(userId, event.getId())) {
                    Toast.makeText(getContext(), "Already in Favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addFavorite(userId, event.getId());
                    Toast.makeText(getContext(), "Added to Favorites!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReserveClick(Event event) {
                ReservationFragment reservationFragment = new ReservationFragment(event);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, reservationFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}