package com.example.prohieu.food;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.prohieu.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import layout.CalorieFragment;

public class Home extends AppCompatActivity implements Profile.OnFragmentInteractionListener, FoodTracker.OnFragmentInteractionListener, CalorieFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    private SearchView searchView;
    private ImageView logo;

    Fragment profileFragment;
    Fragment trackerFragment;
    Fragment searchFragment;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth firebaseAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    searchView.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.VISIBLE);
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.hide(profileFragment).hide(trackerFragment);
                    fragmentTransaction.commit();
                    return true;
                    /*
                case R.id.navigation_home:
                    searchView.setVisibility(View.INVISIBLE);
                    logo.setVisibility(View.INVISIBLE);
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.show(trackerFragment);
                    fragmentTransaction.hide(profileFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_profile:
                    searchView.setVisibility(View.INVISIBLE);
                    logo.setVisibility(View.INVISIBLE);
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.show(profileFragment).hide(trackerFragment);
                    fragmentTransaction.commit();
                    return true;*/
            }
            return false;
        }

    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logo = (ImageView) findViewById(R.id.nutritionixAPIImage);
        logo.setVisibility(View.INVISIBLE);
        Picasso.get().load(R.string.nutriUrl).into(logo);

        firebaseAuth = FirebaseAuth.getInstance();

        fm = getSupportFragmentManager();
        profileFragment = fm.findFragmentById(R.id.profileFragmentLayout);
        if (profileFragment == null && firebaseAuth.getCurrentUser() != null) {
            profileFragment = Profile.newInstance(firebaseAuth.getCurrentUser().getUid());
            Fade fade = new Fade();
            fade.setDuration(1000);
            profileFragment.setEnterTransition(fade);
            profileFragment.setExitTransition(fade);
        }

        //FoodTracker = fm.findFragmentById(R.id.profileFragmentLayout);
        if (trackerFragment == null && firebaseAuth.getCurrentUser() != null) {
            trackerFragment = FoodTracker.newInstance(firebaseAuth.getCurrentUser().getUid());
            Fade fade = new Fade();
            fade.setDuration(1000);
            trackerFragment.setEnterTransition(fade);
            trackerFragment.setExitTransition(fade);
        }

//        searchFragment = fm.findFragmentById(R.id.searchFragmentLayout);
//        if (searchFragment == null){
//            searchFragment = SearchContainer.newInstance();
//            Fade fade = new Fade();
//            fade.setDuration(500);
//            searchFragment.setExitTransition(fade);
//        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.home_search_bar);
        searchView.setVisibility(View.INVISIBLE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        /*fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, profileFragment).add(R.id.fragmentContainer, trackerFragment);
        fragmentTransaction.hide(profileFragment);
        fragmentTransaction.show(trackerFragment);
        fragmentTransaction.commit();*/


        //BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchView.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(profileFragment).hide(trackerFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}
