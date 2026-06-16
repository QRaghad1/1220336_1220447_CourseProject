package com.example.a1220336_1220447_courseproject.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a1220336_1220447_courseproject.R;

public class ContactFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        Button callButton = view.findViewById(R.id.callButton);
        Button locateButton = view.findViewById(R.id.locateButton);
        Button emailButton = view.findViewById(R.id.emailButton);

        callButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+970591234567"));
            startActivity(intent);
        });

        locateButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:31.9038,35.2034?q=Birzeit+University"));
            startActivity(intent);
        });

        emailButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:info@birzeit.edu"));
            startActivity(intent);
        });

        return view;
    }
}