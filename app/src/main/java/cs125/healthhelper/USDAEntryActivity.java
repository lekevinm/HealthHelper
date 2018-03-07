package cs125.healthhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by sean1 on 3/7/2018.
 */

public class USDAEntryActivity  extends AppCompatActivity{

    private Food food;
    private TextView info;
    public boolean gotInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usda_entry);

        food = (Food) getIntent().getSerializableExtra("TheFood");
        info = findViewById(R.id.foodinfo);
        info.setText("Fetching info on " + food.name);

        gotInfo = false;
        getFoodInfo();
    }

    private void getFoodInfo(){
        USDAFoodClient client = new USDAFoodClient(food, this);
        client.execute();
    }

    public void updateTextView(){
        if(gotInfo) {
            info.setText(food.name + "\n" +
                    "per 100grams:\n" +
                    food.getBcals() + " kcal\n" +
                    food.getBcarbs() + "g carbs\n" +
                    food.getBprotein() + "g protein\n" +
                    food.getBfat() + "g fat\n"

            );
        }

        else{
            info.setText("No info found");
        }
    }

}
