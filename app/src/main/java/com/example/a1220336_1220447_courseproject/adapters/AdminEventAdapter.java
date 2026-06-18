package com.example.a1220336_1220447_courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.models.Event;

import java.util.List;

public class AdminEventAdapter extends ArrayAdapter<Event> {

    public interface OnEventActionListener {
        void onEditClick(Event event);
        void onDeleteClick(Event event);
    }

    private final Context context;
    private final List<Event> events;
    private final OnEventActionListener listener;

    public AdminEventAdapter(Context context, List<Event> events, OnEventActionListener listener) {
        super(context, R.layout.item_admin_event, events);
        this.context = context;
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_admin_event, parent, false);
        }

        Event event = events.get(position);

        TextView tvTitle = view.findViewById(R.id.tvAdminEventTitle);
        TextView tvSubtitle = view.findViewById(R.id.tvAdminEventSubtitle);
        Button btnDelete = view.findViewById(R.id.btnDeleteAdminEvent);

        tvTitle.setText(event.getTitle());
        tvSubtitle.setText(event.getCategory() + " - " + event.getDate());

        view.setOnClickListener(v -> listener.onEditClick(event));
        btnDelete.setOnClickListener(v -> listener.onDeleteClick(event));

        return view;
    }
}