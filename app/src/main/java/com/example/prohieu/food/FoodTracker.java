package com.example.prohieu.food;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.prohieu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import layout.CalorieFragment;
import objects.Food;
import objects.FoodAdapter;
import objects.User;

public class FoodTracker extends Fragment implements CalorieFragment.OnFragmentInteractionListener{
    ListView trackerList;
    ArrayList<Food> savedFoodList;
    ArrayList<String> foodUIDList = new ArrayList<String>();
    private DatabaseReference dbreference;
    private DatabaseReference userReference;
    FoodAdapter arrayAdapter;
    int sum;
    int goal;

    CalorieFragment calorieFragment;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    private String muid;

    private Profile.OnFragmentInteractionListener mListener;

    public FoodTracker() {
    }

    public static FoodTracker newInstance(String uid) {
        FoodTracker fragment = new FoodTracker();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dbreference = FirebaseDatabase.getInstance().getReference().child("UserDailyFoods").child((String) getArguments().get("uid"));
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child((String) getArguments().get("uid"));
        View v = inflater.inflate(R.layout.activity_food_tracker, container, false);
        trackerList = (ListView) v.findViewById(R.id.trackerList);
        savedFoodList = new ArrayList<Food>();
        arrayAdapter = new FoodAdapter(getContext(), android.R.layout.simple_list_item_1, savedFoodList);
        trackerList.setAdapter(arrayAdapter);
        fm = getFragmentManager();
        updateList();
        //goal = 1800;
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
        if (context instanceof Profile.OnFragmentInteractionListener) {
            mListener = (Profile.OnFragmentInteractionListener) context;
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


    private void updateList() {
        String uid = (String) getArguments().get("uid");
        ValueEventListener dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Food> foods = new ArrayList<Food>();
                sum = 0;
                foodUIDList.clear();

                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    Food f = foodSnapshot.getValue(Food.class);
                    foodUIDList.add(foodSnapshot.getKey());
                    if (f != null) {
                        foods.add(f);
                        sum += f.getNf_calories();
                    } else {
                        System.out.println("nulllll");
                    }
                    System.out.println("foodnamefb " + f.getFood_name());
                }
                System.out.println(sum);
                savedFoodList.clear();
                savedFoodList.addAll(foods);
                arrayAdapter.notifyDataSetChanged();

                if (calorieFragment == null) {
                    calorieFragment = CalorieFragment.newInstance((String) getArguments().get("uid"), goal, sum);
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.add(R.id.calorieFragmentContainer, calorieFragment);
                    fragmentTransaction.show(calorieFragment);
                    fragmentTransaction.commit();
                    ValueEventListener userCalListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User u = dataSnapshot.getValue(User.class);
                            goal = u.getDailyCalorieGoal();
                            System.out.println(goal + " goal");
                            if (calorieFragment != null) {
                                try {

                                } catch (Exception e) {

                                }
                                calorieFragment.setText(sum, goal);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    userReference.addValueEventListener(userCalListener);
                } else {
                    calorieFragment.setText(sum, goal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbreference.addValueEventListener(dataListener);


    }
}