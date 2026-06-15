package com.example.a1220336_1220447_courseproject.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class EventsFragment extends Fragment {

    RecyclerView recyclerView;
    EventAdapter adapter;
    DatabaseHelper dbHelper;
    List<Event> allEvents, filteredEvents;
    EditText etSearch;
    Spinner spinnerFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerEvents);
        etSearch = view.findViewById(R.id.etSearch);
        spinnerFilter = view.findViewById(R.id.spinnerFilter);

        dbHelper = new DatabaseHelper(getContext());
        allEvents = dbHelper.getAllEvents();
        filteredEvents = new ArrayList<>(allEvents);

        adapter = new EventAdapter(getContext(), filteredEvents, new EventAdapter.OnEventClickListener() {
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
                // userId هنا مؤقت، رح نربطه بالـ session لاحقاً
                dbHelper.addFavorite(1, event.getId());
                Toast.makeText(getContext(), "Added to Favorites!", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents(s.toString(), spinnerFilter.getSelectedItem().toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Filter spinner
        List<String> categories = new ArrayList<>();
        categories.add("All");
        for (Event e : allEvents) {
            if (!categories.contains(e.getCategory())) categories.add(e.getCategory());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);

        return view;
    }

    private void filterEvents(String query, String category) {
        filteredEvents.clear();
        for (Event e : allEvents) {
            boolean matchQuery = e.getTitle().toLowerCase().contains(query.toLowerCase());
            boolean matchCategory = category.equals("All") || e.getCategory().equals(category);
            if (matchQuery && matchCategory) filteredEvents.add(e);
        }
        adapter.updateList(filteredEvents);
    }
}