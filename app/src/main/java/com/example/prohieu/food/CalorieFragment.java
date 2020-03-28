package com.example.prohieu.food;

import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.prohieu.R;

public class CalorieFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView calorieGoal;
    TextView foodCalories;
    TextView caloriesRemaining;

    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public CalorieFragment() {
    }

    public static CalorieFragment newInstance(String param1, int calorieGoal, int caloriesFromFood) {
        CalorieFragment fragment = new CalorieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, calorieGoal);
        args.putInt("caloriesFromFood", caloriesFromFood);
        System.out.println(caloriesFromFood + "sumcalfrag");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calorie, container, false);

        foodCalories = (TextView) v.findViewById(R.id.foodAmount);
        calorieGoal = (TextView) v.findViewById(R.id.goalAmount);
        caloriesRemaining = (TextView) v.findViewById(R.id.caloriesRemaining);

        animateTextView(0, getArguments().getInt(ARG_PARAM2), calorieGoal);
        animateTextView(0, getArguments().getInt("caloriesFromFood"), foodCalories);
        animateTextView(0, getArguments().getInt(ARG_PARAM2) - getArguments().getInt("caloriesFromFood"), caloriesRemaining);
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setText(int calories, int maxCalories) {
        try {
            wait(2500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (calorieGoal != null && foodCalories != null && caloriesRemaining != null) {
            animateTextView(0, maxCalories, calorieGoal);
            animateTextView(0, calories, foodCalories);
            animateTextView(0, maxCalories - calories, caloriesRemaining);
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

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textview.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
