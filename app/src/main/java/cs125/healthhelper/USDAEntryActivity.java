package cs125.healthhelper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by sean1 on 3/7/2018.
 */

public class USDAEntryActivity  extends AppCompatActivity {

    private Food food;
    private TextView food_text;
    private TextView info_text;
    private TextView nutrient_text;
    private TextInputEditText input_text;
    private EditText dateField;

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userID;

    public boolean gotInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usda_entry);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        food = (Food) getIntent().getSerializableExtra("TheFood");
        food_text = findViewById(R.id.food_text);
        info_text = findViewById(R.id.info_text);
        nutrient_text = findViewById(R.id.nutrient_text);
        input_text = findViewById(R.id.grams_text);

        //create a listener on the grams consumed input
        input_text.addTextChangedListener(new TextWatcher() {
            //update the grams on Food each time value changes
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateNutrientText(charSequence);
            }

            //unused functions that need to be implemented
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        dateField = (EditText)findViewById(R.id.dateInput);

        dateField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(USDAEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                int s = monthOfYear + 1;
                                String dateString =  year + "/" + s + "/" + dayOfMonth;
                                dateField.setText(""+dateString);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                dpd.show();
            }
        });


        food_text.setText("Fetching info on " + food.name);
        gotInfo = false;
        getFoodInfo();
    }

    private void getFoodInfo() {
        USDAFoodClient client = new USDAFoodClient(food, this);
        client.execute();
    }

    public void loadFoodInfo() {
        if (gotInfo) {
            food_text.setText(food.name);
            info_text.setText("Serving Size: 100g\n" +
                    food.getBcals() + " kcal\n" +
                    food.getBcarbs() + "g carbs\n" +
                    food.getBprotein() + "g protein\n" +
                    food.getBfat() + "g fat\n"
            );


        } else {
            food_text.setText("No info found");
        }
    }

    private void updateNutrientText(CharSequence ch) {
        Float grams;
        if (ch.length() == 0) {
            grams = 0f;
        } else {
            grams = Float.parseFloat(ch.toString());
        }
        food.amountConsumed(grams);

        nutrient_text.setText("Consumed: " + grams.toString() + "g\n" +
                food.calories + " kcal\n" +
                food.carbs + "g carbs\n" +
                food.protein + "g protein\n" +
                food.fat + "g fat\n"
        );
    }

    //
    public void recordMeal(View view) {

        String dateString = dateField.getText().toString();

        String key = mDatabase.child("foodLog").child(userID).child(dateString).push().getKey();
        mDatabase.child("foodLog").child(userID).child(dateString).child(key).setValue(food);
        startActivity(new Intent(USDAEntryActivity.this, MainActivity.class));

    }
}
