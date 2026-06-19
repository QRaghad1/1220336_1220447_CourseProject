package com.example.a1220336_1220447_courseproject.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
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
import com.example.a1220336_1220447_courseproject.database.DatabaseHelper;
import com.example.a1220336_1220447_courseproject.models.Event;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private OnEventClickListener listener;
    private DatabaseHelper dbHelper;
    private int userId;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface OnEventClickListener {
        void onEventClick(Event event);
        void onFavoriteClick(Event event);
    }

    public EventAdapter(Context context, List<Event> eventList, OnEventClickListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
        this.dbHelper = DatabaseHelper.getInstance(context);
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        this.userId = prefs.getInt("userId", -1);
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

        // Reset to placeholder immediately to avoid showing the wrong image
        // while this row's image is still downloading (RecyclerView view reuse)
        holder.imgEvent.setImageResource(R.mipmap.ic_launcher);
        loadImageManually(event.getImage(), holder.imgEvent);

        // Favorite icon state
        boolean isFav = dbHelper.isFavorite(userId, event.getId());
        holder.btnFavorite.setImageResource(isFav
                ? android.R.drawable.btn_star_big_on
                : android.R.drawable.btn_star_big_off);

        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));

        // Bounce animation on favorite click
        holder.btnFavorite.setOnClickListener(v -> {
            Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
            holder.btnFavorite.startAnimation(bounce);
            listener.onFavoriteClick(event);

            // Toggle icon immediately for instant feedback
            boolean nowFav = dbHelper.isFavorite(userId, event.getId());
            holder.btnFavorite.setImageResource(nowFav
                    ? android.R.drawable.btn_star_big_on
                    : android.R.drawable.btn_star_big_off);
        });

        // Slide-up animation
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        holder.itemView.startAnimation(animation);
    }

    /**
     * Downloads and decodes an image on a background thread, then posts it
     * back to the ImageView on the main thread. Tags the ImageView with the
     * URL being loaded so that if the view gets recycled before the download
     * finishes, we don't accidentally set a stale image on the wrong row.
     */
    private void loadImageManually(String imageUrl, ImageView imageView) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imageView.setImageResource(R.mipmap.ic_launcher);
            return;
        }

        imageView.setTag(imageUrl);

        new Thread(() -> {
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setDoInput(true);
                conn.connect();

                InputStream input = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                input.close();
            } catch (Exception e) {
                bitmap = null; // will fall back to placeholder on main thread
            }

            Bitmap finalBitmap = bitmap;
            mainHandler.post(() -> {
                // Only apply the image if this ImageView is still meant to show this URL
                if (imageUrl.equals(imageView.getTag())) {
                    if (finalBitmap != null) {
                        imageView.setImageBitmap(finalBitmap);
                    } else {
                        imageView.setImageResource(R.mipmap.ic_launcher);
                    }
                }
            });
        }).start();
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