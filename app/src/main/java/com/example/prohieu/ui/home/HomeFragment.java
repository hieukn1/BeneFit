package com.example.prohieu.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.prohieu.R;
import com.example.prohieu.StepCounter.stepcounter;
import com.example.prohieu.alarm.alarm;
import com.example.prohieu.food.Home;
import com.example.prohieu.map.MapsActivity;
import com.example.prohieu.profile.ProfilePage;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // profile
        ImageButton button = root.findViewById(R.id.profile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfilePage.class);
                startActivity(intent);
            }
        });

        // food
        ImageButton button1 = root.findViewById(R.id.food);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
            }
        });

        // workout
        ImageButton button2 = root.findViewById(R.id.workout);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), stepcounter.class);
                startActivity(intent);
            }
        });

        // alarm
        ImageButton button3 = root.findViewById(R.id.alarm);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), alarm.class);
                startActivity(intent);
            }
        });

        // map
        ImageButton button4 = root.findViewById(R.id.map);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}