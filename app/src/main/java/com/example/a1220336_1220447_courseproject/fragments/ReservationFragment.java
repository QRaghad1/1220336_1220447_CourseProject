package com.example.a1220336_1220447_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;
import com.example.a1220336_1220447_courseproject.models.Reservation;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationFragment extends Fragment {

    private Event event;
    private DatabaseHelper dbHelper;

    public ReservationFragment(Event event) {
        this.event = event;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        EditText etQuantity = view.findViewById(R.id.etQuantity);
        Spinner spinnerType = view.findViewById(R.id.spinnerType);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        dbHelper = DatabaseHelper.getInstance(getContext());

        List<String> types = Arrays.asList("Online", "In-Person", "Hybrid");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> {
            String qtyStr = etQuantity.getText().toString().trim();
            if (qtyStr.isEmpty()) {
                etQuantity.setError("Required");
                return;
            }
            int quantity = Integer.parseInt(qtyStr);
            if (quantity <= 0) {
                etQuantity.setError("Must be at least 1");
                return;
            }

            // Get actual logged in userId
            SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);

            if (userId == -1) {
                Toast.makeText(getContext(), "Session error. Please login again.", Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String type = spinnerType.getSelectedItem().toString();

            Reservation reservation = new Reservation(0, userId, event.getId(), date, quantity, type, "Confirmed");
            long result = dbHelper.addReservation(reservation);

            if (result != -1) {
                Toast.makeText(getContext(), "Reservation Confirmed!", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Reservation failed.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}