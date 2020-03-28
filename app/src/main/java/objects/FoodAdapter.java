package objects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import com.example.prohieu.R;

public class FoodAdapter extends ArrayAdapter<Food> {

    private Context c;
    private ArrayList<Food> foods;

    public FoodAdapter(Context c, int resId, ArrayList<Food> foods) {
        super(c, resId, foods);
        this.c = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food f = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_food, parent, false);
        }

        // Lookup view for data population
        TextView foodName = (TextView) convertView.findViewById(R.id.foodname_text);
        TextView calories = (TextView) convertView.findViewById(R.id.calorie_text);
        TextView servingSize = (TextView) convertView.findViewById(R.id.serving_text);
        TextView brandName = (TextView) convertView.findViewById(R.id.brandname_text);
        ImageView foodThumb = (ImageView) convertView.findViewById(R.id.image_list);
        // Populate the data into the template view using the data object
        foodName.setText(f.getFood_name());
        calories.setText(Double.toString(f.getNf_calories()) + " cal");
        if (f.getBrand_name() != null) {
            brandName.setText(f.getBrand_name());
        } else {
            brandName.setText(R.string.common);
        }
        if (f.getServing_unit() != null) {
            servingSize.setText(f.getServing_qty() + " " + f.getServing_unit());
        }
        if (f.getImage() != null && !f.getImage().equals("")) {
            Picasso.get().load(f.getImage()).into(foodThumb);
        }
        // Return the completed view to render on screen
        return convertView;


    }


}
