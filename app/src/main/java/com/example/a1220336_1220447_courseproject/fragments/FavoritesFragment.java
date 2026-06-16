package com.example.a1220336_1220447_courseproject.fragments;

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

import java.util.List;

public class FavoritesFragment extends Fragment {

    RecyclerView recyclerView;
    EventAdapter adapter;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recyclerFavorites);
        dbHelper = new DatabaseHelper(getContext());

        // userId مؤقت = 1، رح نربطه بالـ session لاحقاً
        List<Event> favorites = dbHelper.getFavoriteEvents(1);

        adapter = new EventAdapter(getContext(), favorites, new EventAdapter.OnEventClickListener() {
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
                dbHelper.removeFavorite(1, event.getId());
                Toast.makeText(getContext(), "Removed from Favorites!", Toast.LENGTH_SHORT).show();
                // تحديث القائمة
                List<Event> updated = dbHelper.getFavoriteEvents(1);
                adapter.updateList(updated);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}