package cs125.healthhelper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FoodEntryActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText servingInput;
    private EditText calorieInput;
    private EditText carbsInput;
    private EditText proteinInput;
    private EditText fatInput;
    private DatabaseReference mDatabase;

    private FirebaseUser user;
    private String userID;
    private EditText dateField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameInput = findViewById(R.id.foodInput);
        servingInput = findViewById(R.id.servingInput);
        calorieInput = findViewById(R.id.calorieInput);
        carbsInput = findViewById(R.id.carbInput);
        proteinInput = findViewById(R.id.proteinInput);
        fatInput = findViewById(R.id.fatInput);

        dateField = findViewById(R.id.dateInput);

        dateField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(FoodEntryActivity.this,
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
    }

    public void submitEntry(View view){

        String name = nameInput.getText().toString();
        int servingSize = Integer.parseInt(servingInput.getText().toString());
        int calories =  Integer.parseInt(calorieInput.getText().toString());
        int carbs =  Integer.parseInt(carbsInput.getText().toString());
        int protein =  Integer.parseInt(proteinInput.getText().toString());
        int fat =  Integer.parseInt(fatInput.getText().toString());

        String dateString = dateField.getText().toString();
        Food foodEntry = new Food(name, servingSize, calories, carbs, protein, fat);
        String key = mDatabase.child("foodLog").child(userID).child(dateString).push().getKey();
        mDatabase.child("foodLog").child(userID).child(dateString).child(key).setValue(foodEntry);
        startActivity(new Intent(FoodEntryActivity.this, MainActivity.class));



    }
}
