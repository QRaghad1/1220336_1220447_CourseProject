package com.example.a1220336_1220447_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;

public class EditEventFragment extends Fragment {

    private final Event event;
    private DatabaseHelper dbHelper;

    EditText etEditTitle, etEditCategory, etEditDate, etEditTime,
            etEditLocation, etEditSeats, etEditDescription;
    Button btnSaveEdit, btnCancelEdit;

    public EditEventFragment(Event event) {
        this.event = event;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        dbHelper = DatabaseHelper.getInstance(getContext());

        etEditTitle = view.findViewById(R.id.etEditTitle);
        etEditCategory = view.findViewById(R.id.etEditCategory);
        etEditDate = view.findViewById(R.id.etEditDate);
        etEditTime = view.findViewById(R.id.etEditTime);
        etEditLocation = view.findViewById(R.id.etEditLocation);
        etEditSeats = view.findViewById(R.id.etEditSeats);
        etEditDescription = view.findViewById(R.id.etEditDescription);
        btnSaveEdit = view.findViewById(R.id.btnSaveEdit);
        btnCancelEdit = view.findViewById(R.id.btnCancelEdit);

        // Pre-fill current values
        etEditTitle.setText(event.getTitle());
        etEditCategory.setText(event.getCategory());
        etEditDate.setText(event.getDate());
        etEditTime.setText(event.getTime());
        etEditLocation.setText(event.getLocation());
        etEditSeats.setText(String.valueOf(event.getSeats()));
        etEditDescription.setText(event.getDescription());

        btnSaveEdit.setOnClickListener(v -> saveChanges());
        btnCancelEdit.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void saveChanges() {
        String title = etEditTitle.getText().toString().trim();
        String category = etEditCategory.getText().toString().trim();
        String date = etEditDate.getText().toString().trim();
        String time = etEditTime.getText().toString().trim();
        String location = etEditLocation.getText().toString().trim();
        String seatsStr = etEditSeats.getText().toString().trim();
        String description = etEditDescription.getText().toString().trim();

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

        event.setTitle(title);
        event.setCategory(category);
        event.setDate(date);
        event.setTime(time);
        event.setLocation(location);
        event.setSeats(seats);
        event.setDescription(description);

        dbHelper.updateEvent(event);
        Toast.makeText(getContext(), "Event Updated!", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}