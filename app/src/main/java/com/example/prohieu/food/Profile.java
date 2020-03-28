package com.example.prohieu.food;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prohieu.R;
import com.example.prohieu.profile.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import objects.User;

public class Profile extends Fragment {
    private static final String ARG_UID = "userUID";
    private static final String ARG_PARAM2 = "param2";

    private String muid;
    private SeekBar seekBar;
    private Button saveChanges;
    private TextView profileCalorieTextView;
    private int dailyCalorieGoal = 0;
    private String profileName;
    private TextView profileNameTV;
    private TextView profileEmailTV;
    private String userEmail;
    private FirebaseAuth auth;
    DatabaseReference userReference;

    private OnFragmentInteractionListener mListener;

    public Profile() {
    }

    public static Profile newInstance(String uid) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            muid = getArguments().getString(ARG_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_food, container, false);
        userReference = FirebaseDatabase.getInstance().getReference("Member").child(auth.getCurrentUser().getDisplayName());
        profileCalorieTextView = (TextView) v.findViewById(R.id.profile_seekbar_response);
        profileNameTV = (TextView) v.findViewById(R.id.profile_user_name);
        profileEmailTV = (TextView) v.findViewById(R.id.profile_user_email);
        seekBar = (SeekBar) v.findViewById(R.id.profile_calorie_chooser);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dailyCalorieGoal = ((int) Math.round(i / 10.0)) * 10;
                seekBar.setProgress(dailyCalorieGoal);
                profileCalorieTextView.setText(dailyCalorieGoal + " Cal");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        ValueEventListener userCalListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member mem = dataSnapshot.getValue(Member.class);
                User u = new User(mem.getName());
                dailyCalorieGoal = u.getDailyCalorieGoal();
                profileCalorieTextView.setText(Integer.toString(dailyCalorieGoal) + " Cal");
                seekBar.setProgress(dailyCalorieGoal);
                profileName = u.getName();
                userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                profileEmailTV.setText(userEmail);
                profileNameTV.setText(profileName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        userReference.addValueEventListener(userCalListener);
        saveChanges = (Button) v.findViewById(R.id.saveChanges_button);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                userReference.child("dailyCalorieGoal").setValue(dailyCalorieGoal);
            }
        });
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
