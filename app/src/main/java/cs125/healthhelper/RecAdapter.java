package cs125.healthhelper;

import android.content.Context;
import android.graphics.Movie;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cs125.healthhelper.FoodRecommendation;
import cs125.healthhelper.R;

/**
 * Created by wilson on 3/9/2018.
 */

public class RecAdapter extends ArrayAdapter<FoodRecommendation> implements View.OnClickListener {

    private ArrayList<FoodRecommendation> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView recipeName;
    }

    public RecAdapter(ArrayList<FoodRecommendation> data, Context context) {
        super(context, R.layout.recommendation, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        // Placeholder to allow override
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FoodRecommendation foodRec = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.recommendation, parent, false);
            viewHolder.recipeName = (TextView) convertView.findViewById(R.id.recommended_name);

            result = convertView;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.recipeName.setText(foodRec.getRecipeName());
        // Return the completed view to render on screen
        return convertView;
    }
}
