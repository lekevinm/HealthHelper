package cs125.healthhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class GoalActivity extends AppCompatActivity{

    //private Spinner spinner;
    //private static final String[]paths = {"Fat", "Carbs", "Protein"};
    private EditText calories;
    private EditText fat;
    private EditText carbs;
    private EditText protein;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        calories = findViewById(R.id.calorieGoal);
        fat = findViewById(R.id.fatGoal);
        carbs = findViewById(R.id.carbGoal);
        protein = findViewById(R.id.proteinGoal);

        setGoalFields();

        //spinner = (Spinner)findViewById(R.id.NutrientType);
        //ArrayAdapter<String>adapter = new ArrayAdapter<String>(GoalActivity.this,
        //        android.R.layout.simple_spinner_item,paths);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        setGoalFields();
    }

    public void setGoal(View view){
        int calorieGoal = Integer.parseInt(calories.getText().toString());
        int fatGoal = Integer.parseInt(fat.getText().toString());
        int carbGoal = Integer.parseInt(carbs.getText().toString());
        int proteinGoal = Integer.parseInt(protein.getText().toString());

        UserGoals newUserGoal = new UserGoals(calorieGoal, fatGoal, carbGoal, proteinGoal);
        mDatabase.child("goals").child(userID).setValue(newUserGoal);

    }

    private void setGoalFields()
    {
        DatabaseReference goals = mDatabase.child("goals");
        goals.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(userID).exists())
                {
                    UserGoals userGoals = snapshot.child(userID).getValue(UserGoals.class);
                    int goalCalories = userGoals.calorieGoal;
                    int goalFat = userGoals.fatGoal;
                    int goalCarbs = userGoals.carbGoal;
                    int goalProtein = userGoals.proteinGoal;

                    calories.setText(String.format(Locale.getDefault(),"%d", goalCalories));
                    fat.setText(String.format(Locale.getDefault(),"%d", goalFat));
                    carbs.setText(String.format(Locale.getDefault(),"%d", goalCarbs));
                    protein.setText(String.format(Locale.getDefault(),"%d", goalProtein));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

}
