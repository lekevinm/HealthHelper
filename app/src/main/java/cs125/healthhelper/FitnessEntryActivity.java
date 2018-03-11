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

public class FitnessEntryActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText durationInput;
    private EditText calorieInput;
    private EditText dateField;

    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_entry);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameInput = findViewById(R.id.nameInput);
        durationInput = findViewById(R.id.durationInput);
        calorieInput = findViewById(R.id.calorieInput);
        dateField = findViewById(R.id.dateInput);

        dateField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(FitnessEntryActivity.this,
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


        String exerciseName = nameInput.getText().toString();
        int duration = Integer.parseInt(durationInput.getText().toString());
        int calories =  Integer.parseInt(calorieInput.getText().toString());

        String dateString = dateField.getText().toString();
        //Food foodEntry = new Food(name, servingSize, calories, carbs, protein, fat);
        Exercise exerciseEntry = new Exercise(exerciseName, duration, calories);
        String key = mDatabase.child("exerciseLog").child(userID).child(dateString).push().getKey();
        mDatabase.child("exerciseLog").child(userID).child(dateString).child(key).setValue(exerciseEntry);
        startActivity(new Intent(FitnessEntryActivity.this, MainActivity.class));
    }
}
