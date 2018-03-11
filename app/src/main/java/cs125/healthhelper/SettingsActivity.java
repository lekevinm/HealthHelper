package cs125.healthhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    ListView listview;
    List allergies;
    String [] allergyOptions = new String[]{
            "Eggs",
            "Milk",
            "Peanuts",
            "Tree nuts",
            "Fish",
            "Shellfish",
            "Wheat",
            "Soy"
    };
    SparseBooleanArray sparseBooleanArray;
    Button set;
    EditText userHeight;
    EditText userWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userHeight = findViewById(R.id.foodInput);
        userWeight = findViewById(R.id.servingInput);

        listview = findViewById(R.id.listView);
        allergies = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (SettingsActivity.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1,
                        allergyOptions);

        listview.setAdapter(adapter);

        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    public void setProfile(View view){


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        int height = Integer.parseInt(userHeight.getText().toString());
        int weight = Integer.parseInt(userWeight.getText().toString());

        getAllergies();
        UserProfile userProfile = new UserProfile(height, weight, allergies);
        mDatabase.child("profiles").child(userID).setValue(userProfile);
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));


    }

    protected void getAllergies()
    {
        for (int i = 0; i < listview.getCount(); i++)
        {
            if (listview.isItemChecked(i))
            {
                allergies.add(allergyOptions[i]);
                //allergies.add()
            }
        }
    }

//    public void setWeight(View view){
//    }
//
//    public void addAllergy(View view){
//    }
}
