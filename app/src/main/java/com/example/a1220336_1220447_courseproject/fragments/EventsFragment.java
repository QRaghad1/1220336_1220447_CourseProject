package com.example.a1220336_1220447_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.adapters.EventAdapter;
import com.example.a1220336_1220447_courseproject.api.ApiService;
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
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerEvents);
        etSearch = view.findViewById(R.id.etSearch);
        spinnerFilter = view.findViewById(R.id.spinnerFilter);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        dbHelper = DatabaseHelper.getInstance(getContext());
        allEvents = dbHelper.getAllEvents();

        if (allEvents.isEmpty()) {
            fetchEventsFromApi();
        }

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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEvents(s.toString(), spinnerFilter.getSelectedItem().toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        setupCategorySpinner();

        return view;
    }

    private void fetchEventsFromApi() {
        ApiService apiService = new ApiService(getContext());
        apiService.fetchAndStoreEvents(new ApiService.FetchCallback() {
            @Override
            public void onSuccess() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        allEvents = dbHelper.getAllEvents();
                        filteredEvents.clear();
                        filteredEvents.addAll(allEvents);
                        adapter.updateList(filteredEvents);
                        setupCategorySpinner();
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed to sync events: " + error, Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        for (Event e : allEvents) {
            if (!categories.contains(e.getCategory())) categories.add(e.getCategory());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterEvents(etSearch.getText().toString(), parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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