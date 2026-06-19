package com.example.a1220336_1220447_courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(Event event);
        void onFavoriteClick(Event event);
    }

    public EventAdapter(Context context, List<Event> eventList, OnEventClickListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.tvTitle.setText(event.getTitle());
        holder.tvCategory.setText(event.getCategory());
        holder.tvDate.setText(event.getDate() + " " + event.getTime());
        holder.tvLocation.setText(event.getLocation());
        holder.tvSeats.setText("Seats: " + event.getSeats());

        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));

        // Bounce animation on favorite click
        holder.btnFavorite.setOnClickListener(v -> {
            Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
            holder.btnFavorite.startAnimation(bounce);
            listener.onFavoriteClick(event);
        });

        // Slide-up animation
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateList(List<Event> newList) {
        eventList = newList;
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDate, tvLocation, tvSeats;
        ImageButton btnFavorite;
        ImageView imgEvent;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            imgEvent = itemView.findViewById(R.id.imgEvent);
        }
    }
}