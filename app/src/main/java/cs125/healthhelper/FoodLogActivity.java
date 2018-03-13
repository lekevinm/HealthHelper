package cs125.healthhelper;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class FoodLogActivity extends AppCompatActivity {

    private ListView foodListView;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<String> listKeys = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private Button deleteButton;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;
    private EditText dateField;
    private Boolean searchMode = false;


    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userID;
    private ValueEventListener queryValueListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_log);

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setEnabled(false);
        foodListView = findViewById(R.id.foodListView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("foodLog").child(userID);
        //mDatabase = mDatabase.child("foodLog").child(userID);

        // Allows user to pick date with widget (sets default to current date)
        dateField = findViewById(R.id.dateInput);

        dateField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(FoodLogActivity.this,
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

        // Populates ListView
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                listItems);
        foodListView.setAdapter(adapter);
        foodListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        foodListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        selectedPosition = position;
                        itemSelected = true;
                        deleteButton.setEnabled(true);
                    }
                });

        addChildEventListener();

    }

    private void addChildEventListener()
    {
        ChildEventListener childListener = new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                //query = mDatabase.child("foodLog").child(userID).child(dateString).orderByKey();
                //String dateString = "2018/3/11";
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Food food = childSnapshot.getValue(Food.class);
                    if (food.name == null)
                        break;
                    String foodString = food.name + " (calories: " + food.calories + "kcal, fat: " +
                            food.fat + "g, carbs: " + food.carbs + "g, protein: " + food.protein +
                            "g)";
                    adapter.add(foodString);
                    adapter.notifyDataSetChanged(); //
                    //adapter.add( (String) dataSnapshot.child("description").getValue());
                    listKeys.add(childSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = listKeys.indexOf(key);

                if (index != -1) {
                    listItems.remove(index);
                    listKeys.remove(index);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabase.addChildEventListener(childListener);

    }

    public void changeDate(View view)
    {
        final String dateString = dateField.getText().toString();

        Query query;

        if (!searchMode) {
            //findButton.setText("Clear");
            // .child("foodLog").child("userID").child(stringDate).orderByKey()
            query = mDatabase.child(dateString).orderByKey();
            searchMode = true;
        } else {
            searchMode = false;
            //findButton.setText("Find");
            query = mDatabase.child(dateString).orderByKey();
        }

        if (itemSelected) {
            foodListView.setItemChecked(selectedPosition, false);
            itemSelected = false;
            deleteButton.setEnabled(false);
        }

        queryValueListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                adapter.clear();
                listKeys.clear();

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);
                    String foodString = food.name + " (calories: " + food.calories + "kcal, fat: " +
                            food.fat + "g, carbs: " + food.carbs + "g, protein: " + food.protein +
                            "g)";

                    String key = next.getKey();
                    listKeys.add(key);
                    adapter.add(foodString);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query.addListenerForSingleValueEvent(queryValueListener);
    }

    public void deleteItem(View view)
    {
        String dateString = dateField.getText().toString();
        foodListView.setItemChecked(selectedPosition, false);
        mDatabase.child(dateString).child(listKeys.get(selectedPosition)).removeValue();
    }
}
