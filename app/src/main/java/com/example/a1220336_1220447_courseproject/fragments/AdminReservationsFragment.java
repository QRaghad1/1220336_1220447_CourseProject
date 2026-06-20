package com.example.a1220336_1220447_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;
import com.example.a1220336_1220447_courseproject.models.Reservation;
import com.example.a1220336_1220447_courseproject.models.User;

import java.util.ArrayList;
import java.util.List;

public class AdminReservationsFragment extends Fragment {

    ListView listView;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_reservations, container, false);

        listView = view.findViewById(R.id.listAdminReservations);
        dbHelper = DatabaseHelper.getInstance(requireContext());

        List<Reservation> reservations = dbHelper.getAllReservations();
        List<String> displayList = new ArrayList<>();

        for (Reservation r : reservations) {
            User user = dbHelper.getUserById(r.getUserId());
            Event event = dbHelper.getEventById(r.getEventId());

            String userName = (user != null) ? user.getFirstName() + " " + user.getLastName() : "Unknown User";
            String eventName = (event != null) ? event.getTitle() : "Unknown Event";

            displayList.add("User: " + userName +
                    "\nEvent: " + eventName +
                    "\nDate: " + r.getReservationDate() +
                    "\nQty: " + r.getQuantity() +
                    "\nStatus: " + r.getStatus());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);

        return view;
    }
}