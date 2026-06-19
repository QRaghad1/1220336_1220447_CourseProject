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

    private static final String FEATURED_CATEGORY = "Technology";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFeaturedEvents);
        dbHelper = DatabaseHelper.getInstance(getContext());

        List<Event> allEvents = dbHelper.getAllEvents();
        List<Event> featured = new ArrayList<>();

        for (Event e : allEvents) {
            if (e.getCategory().equalsIgnoreCase(FEATURED_CATEGORY)) {
                featured.add(e);
            }
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
                SharedPreferences prefs = requireContext()
                        .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                int userId = prefs.getInt("userId", -1);
                if (userId == -1) {
                    Toast.makeText(getContext(), "Please login again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbHelper.addFavorite(userId, event.getId());
                Toast.makeText(getContext(), "Added to Favorites!", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
