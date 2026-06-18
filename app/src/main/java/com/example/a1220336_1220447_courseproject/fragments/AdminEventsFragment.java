package com.example.a1220336_1220447_courseproject.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.adapters.AdminEventAdapter;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;

import java.util.List;

public class AdminEventsFragment extends Fragment {

    ListView listView;
    DatabaseHelper dbHelper;
    List<Event> events;
    AdminEventAdapter adapter;
    EditText etTitle, etCategory, etDate, etTime, etLocation, etSeats, etDescription;
    Button btnAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_events, container, false);

        listView = view.findViewById(R.id.listAdminEvents);
        etTitle = view.findViewById(R.id.etAdminTitle);
        etCategory = view.findViewById(R.id.etAdminCategory);
        etDate = view.findViewById(R.id.etAdminDate);
        etTime = view.findViewById(R.id.etAdminTime);
        etLocation = view.findViewById(R.id.etAdminLocation);
        etSeats = view.findViewById(R.id.etAdminSeats);
        etDescription = view.findViewById(R.id.etAdminDescription);
        btnAdd = view.findViewById(R.id.btnAdminAddEvent);

        dbHelper = DatabaseHelper.getInstance(getContext());
        loadEvents();

        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String seatsStr = etSeats.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (title.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(getContext(), "Please fill required fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            int seats;
            try {
                seats = seatsStr.isEmpty() ? 0 : Integer.parseInt(seatsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Seats must be a valid number!", Toast.LENGTH_SHORT).show();
                return;
            }

            Event event = new Event(0, title, description, category, date, time, location, seats, "");
            dbHelper.addEvent(event);
            Toast.makeText(getContext(), "Event Added!", Toast.LENGTH_SHORT).show();
            clearFields();
            loadEvents();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dbHelper != null) {
            loadEvents();
        }
    }

    private void clearFields() {
        etTitle.setText("");
        etCategory.setText("");
        etDate.setText("");
        etTime.setText("");
        etLocation.setText("");
        etSeats.setText("");
        etDescription.setText("");
    }

    private void loadEvents() {
        events = dbHelper.getAllEvents();

        adapter = new AdminEventAdapter(getContext(), events, new AdminEventAdapter.OnEventActionListener() {
            @Override
            public void onEditClick(Event event) {
                EditEventFragment editFragment = new EditEventFragment(event);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_fragment_container, editFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onDeleteClick(Event event) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete \"" + event.getTitle() + "\"?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            dbHelper.deleteEvent(event.getId());
                            Toast.makeText(getContext(), "Event Deleted!", Toast.LENGTH_SHORT).show();
                            loadEvents();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        listView.setAdapter(adapter);
    }
}