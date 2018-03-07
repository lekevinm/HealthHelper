package cs125.healthhelper;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by sean1 on 3/7/2018.
 */

public class USDAEntryActivity  extends AppCompatActivity{

    private Food food;
    private TextView food_text;
    private TextView info_text;
    private TextView nutrient_text;
    private TextInputEditText input_text;

    public boolean gotInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usda_entry);

        food = (Food) getIntent().getSerializableExtra("TheFood");
        food_text = findViewById(R.id.food_text);
        info_text = findViewById(R.id.info_text);
        nutrient_text = findViewById(R.id.nutrient_text);
        input_text = findViewById(R.id.grams_text);

        //create a listener on the grams consumed input
        input_text.addTextChangedListener(new TextWatcher(){
            //update the grams on Food each time value changes
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateNutrientText(charSequence);
            }

            //unused functions that need to be implemented
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        food_text.setText("Fetching info on " + food.name);
        gotInfo = false;
        getFoodInfo();
    }

    private void getFoodInfo(){
        USDAFoodClient client = new USDAFoodClient(food, this);
        client.execute();
    }

    public void loadFoodInfo(){
        if(gotInfo) {
            food_text.setText(food.name);
            info_text.setText("Serving Size: 100g\n" +
                    food.getBcals() + " kcal\n" +
                    food.getBcarbs() + "g carbs\n" +
                    food.getBprotein() + "g protein\n" +
                    food.getBfat() + "g fat\n"
            );


        }

        else{
            food_text.setText("No info found");
        }
    }

    private void updateNutrientText(CharSequence ch){
        Float grams;
        if(ch.length() == 0){
            grams = 0f;
        }
        else{
            grams = Float.parseFloat(ch.toString());
        }
        food.amountConsumed(grams);

        nutrient_text.setText("Consumed: \n" +
                food.calories + " kcal\n" +
                food.carbs + "g carbs\n" +
                food.protein + "g protein\n" +
                food.fat + "g fat\n"
        );
    }
}
