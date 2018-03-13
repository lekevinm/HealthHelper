package cs125.healthhelper;

import android.app.Activity;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by wilso on 3/13/2018.
 */

public class FoodRecPopup extends Activity {

    private FoodRecommendation foodRec;
    private TextView recipeName;
    private TextView ingredients;
    private TextView course;
    private TextView source;
    private TextView totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_rec_popup);

        foodRec = (FoodRecommendation) getIntent().getSerializableExtra("foodRec");
        recipeName = findViewById(R.id.RecipeName);
        ingredients = findViewById(R.id.Ingredients);
        course = findViewById(R.id.Course);
        source = findViewById(R.id.Source);
        totalTime = findViewById(R.id.TotalTime);

        recipeName.setText("  Recipe Name: \n\t" + foodRec.getRecipeName());
        ingredients.setText("  Ingredients: \n\t" + foodRec.getIngredients());
        course.setText("  Course: \n\t" + foodRec.getCourse());
        source.setText("  Source: \n\t" + foodRec.getSource());
        totalTime.setText("  Total Time: \n\t" + foodRec.getTotalTime());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));
    }
}
