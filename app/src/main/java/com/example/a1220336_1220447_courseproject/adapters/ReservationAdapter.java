package com.example.a1220336_1220447_courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;
import com.example.a1220336_1220447_courseproject.models.Reservation;

import java.util.List;

public class ReservationAdapter extends ArrayAdapter<Reservation> {

    private DatabaseHelper dbHelper;

    public ReservationAdapter(@NonNull Context context, @NonNull List<Reservation> objects) {
        super(context, 0, objects);
        dbHelper = DatabaseHelper.getInstance(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reservation, parent, false);
        }

        Reservation reservation = getItem(position);
        TextView tvTitle = convertView.findViewById(R.id.tvResEventTitle);
        TextView tvDate = convertView.findViewById(R.id.tvResDate);
        TextView tvQty = convertView.findViewById(R.id.tvResQty);
        TextView tvType = convertView.findViewById(R.id.tvResType);
        TextView tvStatus = convertView.findViewById(R.id.tvResStatus);

        if (reservation != null) {
            // Fetch event title from DB
            Event event = dbHelper.getEventById(reservation.getEventId());
            if (event != null) {
                tvTitle.setText(event.getTitle());
            } else {
                tvTitle.setText("Unknown Event");
            }

            tvDate.setText("Date: " + reservation.getReservationDate());
            tvQty.setText("Qty: " + reservation.getQuantity());
            tvType.setText("Type: " + reservation.getType());
            tvStatus.setText("Status: " + reservation.getStatus());
        }

        return convertView;
    }
}