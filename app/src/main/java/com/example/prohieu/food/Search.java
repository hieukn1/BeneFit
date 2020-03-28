package com.example.prohieu.food;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prohieu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import objects.DataContainer;
import objects.Food;
import objects.FoodAdapter;


public class Search extends Activity {
    private ListView lv;
    DataContainer outerDC;
    FoodAdapter arrayAdapter;
    ArrayList<Food> concatList = new ArrayList<Food>();
    DatabaseReference dbreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        lv = (ListView) findViewById(R.id.list);
        arrayAdapter = new FoodAdapter(this, android.R.layout.simple_list_item_1, concatList);
        dbreference = FirebaseDatabase.getInstance().getReference().child("UserDailyFoods").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                final Food f = concatList.get(position);

                //Layout stuff
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = inflater.inflate(R.layout.popup_window, null);
                final PopupWindow mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setElevation(5.0f);

                //View Declarations
                ImageButton exitButton = (ImageButton) popupView.findViewById(R.id.popup_exit_button);
                TextView foodName = (TextView) popupView.findViewById(R.id.popup_food_name);
                TextView calories = (TextView) popupView.findViewById(R.id.popup_food_cal);
                TextView brandName = (TextView) popupView.findViewById(R.id.popup_brand_name);
                TextView servingText = (TextView) popupView.findViewById(R.id.popup_food_serving);
                ImageView foodImage = (ImageView) popupView.findViewById(R.id.popup_food_image);
                Button addFood = (Button) popupView.findViewById(R.id.popup_add_food_button);

                //Set texts
                foodName.setText(f.getFood_name().substring(0, 1).toUpperCase() + f.getFood_name().substring(1));
                calories.setText(Double.toString(f.getNf_calories()) + " cal");
                if (f.getBrand_name() != null) {
                    brandName.setText(f.getBrand_name());
                } else {
                    brandName.setText(R.string.common);
                }
                servingText.setText(Double.toString(f.getServing_qty()) + " " + f.getServing_unit());
                Picasso.get().load(f.getImage()).into(foodImage);

                mPopupWindow.setOutsideTouchable(false);
                mPopupWindow.setFocusable(true);
                mPopupWindow.showAtLocation(findViewById(R.id.search_activity_layout), Gravity.CENTER, 0, 0);

                addFood.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //dbreference.push().setValue(f);
                        Toast.makeText(Search.this, "To be implemented", Toast.LENGTH_SHORT).show();
                        mPopupWindow.dismiss();
                    }


                });

                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });
        handleIntent(getIntent());
    }

    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println(query);
            System.out.print("searching for");
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        new HttpAsyncTask().execute(query);
    }

    private void updateList(DataContainer result) {
        concatList.clear();
        if (result != null && result.getBranded() != null) {
            concatList.addAll(result.getBranded());
            concatList.addAll(result.getCommon());
        } else {
            Toast.makeText(this, "No results found!", Toast.LENGTH_SHORT).show();
        }

        lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        outerDC = result;
        arrayAdapter.notifyDataSetChanged();
    }

    private DataContainer GET(String query) {
        DataContainer dc = null;
        String result = "";
        try {
            URL myUrl = new URL("https://trackapi.nutritionix.com/v2/search/instant?query=" + query);
            // create HttpClient
            HttpURLConnection http = (HttpURLConnection) myUrl.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("x-app-id", "92d6b50a");
            http.setRequestProperty("x-app-key", "6755367615b2b4bfc310371e5a375c7e");
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            Gson gson = new Gson();
            dc = gson.fromJson(in, DataContainer.class);
            System.out.println(dc.getBranded().get(0).getFood_name());

            if (in != null) {
            } else {
                result = "Read Error!";
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            dc = null;
        }
        return dc;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, DataContainer> {
        @Override
        protected DataContainer doInBackground(String... query) {
            return GET(query[0]);
        }

        @Override
        protected void onPostExecute(DataContainer result) {
            updateList(result);
        }
    }
}
