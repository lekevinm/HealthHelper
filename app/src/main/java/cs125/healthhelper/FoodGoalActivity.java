package cs125.healthhelper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class FoodGoalActivity extends AppCompatActivity implements OnItemSelectedListener{

    private Spinner spinner;
    private static final String[]paths = {"Fat", "Carbs", "Protein"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_goal);

        spinner = (Spinner)findViewById(R.id.NutrientType);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(FoodGoalActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(this, "NothingSelected", Toast.LENGTH_SHORT).show();
    }


    public void createGoal(View view){
    }
}
