package com.example.a1220336_1220447_courseproject.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.a1220336_1220447_courseproject.R;
import com.example.a1220336_1220447_courseproject.models.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener {
        void onDeleteClick(User user, int position);
    }

    public UserAdapter(Context context, List<User> users, OnDeleteClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @Override
    public int getCount() { return users.size(); }

    @Override
    public Object getItem(int position) { return users.get(position); }

    @Override
    public long getItemId(int position) { return users.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        User user = users.get(position);

        TextView tvName = convertView.findViewById(R.id.tvUserName);
        TextView tvEmail = convertView.findViewById(R.id.tvUserEmail);
        Button btnDelete = convertView.findViewById(R.id.btnDeleteUser);

        tvName.setText(user.getFirstName() + " " + user.getLastName());
        tvEmail.setText(user.getEmail());

        if (user.isAdmin()) {
            btnDelete.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete " + user.getFirstName() + "?")
                        .setPositiveButton("Delete", (dialog, which) -> listener.onDeleteClick(user, position))
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }

        return convertView;
    }
}