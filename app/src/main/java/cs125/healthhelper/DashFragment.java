package cs125.healthhelper;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
//import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class DashFragment extends Fragment {

    private ProgressBar caloriebar;
    private ProgressBar carbbar;
    private ProgressBar fatbar;
    private ProgressBar proteinbar;

    private TextView calorieView;
    private TextView carbView;
    private TextView fatView;
    private TextView proteinView;

    private int dailyCalGain;
    private int dailyCalBurn;
    private int dailyCarb;
    private int dailyFat;
    private int dailyProtein;

    private int goalCalories;
    private int goalFat;
    private int goalCarbs;
    private int goalProtein;

    private Integer[] weeklyCaloriesBurned;
    private Integer[] weeklyCaloriesGained;
    private int counter;
    private int test_counter;

    private LineChart chart;

    private FirebaseUser user;
    private DatabaseReference mFoodDatabase;
    private DatabaseReference mExerciseDatabase;
    private DatabaseReference mGoalDatabase;
    private DatabaseReference goals;
    private String userID;

    private ValueEventListener foodchartQueryValueListener;
    private ValueEventListener exercisechartQueryValueListener;
    private ValueEventListener foodchartQueryValueListener2;
    private ValueEventListener exercisechartQueryValueListener2;
    private ValueEventListener foodchartQueryValueListener3;
    private ValueEventListener exercisechartQueryValueListener3;
    private ValueEventListener foodchartQueryValueListener4;
    private ValueEventListener exercisechartQueryValueListener4;
    private ValueEventListener foodchartQueryValueListener5;
    private ValueEventListener exercisechartQueryValueListener5;
    private ValueEventListener foodchartQueryValueListener6;
    private ValueEventListener exercisechartQueryValueListener6;
    private ValueEventListener foodchartQueryValueListener7;
    private ValueEventListener exercisechartQueryValueListener7;
    private ValueEventListener foodQueryValueListener;
    private ValueEventListener exerciseQueryValueListener;

    private View rootView;
   // private Handler progressBarHandler;

    private Calendar c;
    private String dateString;

    private Query foodQuery;
    private Query exerciseQuery;

    private Query foodchartQuery;
    private Query exercisechartQuery;
    private Query foodchartQuery2;
    private Query exercisechartQuery2;
    private Query foodchartQuery3;
    private Query exercisechartQuery3;
    private Query foodchartQuery4;
    private Query exercisechartQuery4;
    private Query foodchartQuery5;
    private Query exercisechartQuery5;
    private Query foodchartQuery6;
    private Query exercisechartQuery6;
    private Query foodchartQuery7;
    private Query exercisechartQuery7;


    @TargetApi(Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_dash, container, false);

        weeklyCaloriesGained = new Integer[] {0, 0, 0, 0, 0, 0, 0};
        weeklyCaloriesBurned = new Integer[] {0, 0, 0, 0, 0, 0, 0};
        counter = 0;
        dailyCalGain = 0;
        dailyCalBurn = 0;
        dailyCarb = 0;
        dailyFat = 0;
        dailyProtein = 0;

        goalCalories = 0;
        goalFat = 0;
        goalCarbs = 0;
        goalProtein = 0;
        test_counter = 0;

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mFoodDatabase = FirebaseDatabase.getInstance().getReference("foodLog").child(userID);
        mExerciseDatabase = FirebaseDatabase.getInstance().getReference("exerciseLog").child(userID);
        mGoalDatabase = FirebaseDatabase.getInstance().getReference();

        c = Calendar.getInstance();
        dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH);

        foodQuery = mFoodDatabase.child(dateString).orderByKey();
        exerciseQuery = mExerciseDatabase.child(dateString).orderByKey();

        foodQueryValueListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                dailyCalGain = 0;
                dailyCarb = 0;
                dailyFat = 0;
                dailyProtein = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);

                    dailyCalGain += food.calories;
                    dailyCarb  += food.carbs;
                    dailyFat += food.fat;
                    dailyProtein += food.protein;
                    setProgress();
                    setChart();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //foodQuery.addListenerForSingleValueEvent(foodQueryValueListener);
        foodQuery.addValueEventListener(foodQueryValueListener);

        exerciseQueryValueListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                dailyCalBurn = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Exercise exercise = next.getValue(Exercise.class);

                    dailyCalBurn += exercise.caloriesBurned;
                    setProgress();
                    setChart();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //exerciseQuery.addListenerForSingleValueEvent(exerciseQueryValueListener);
        exerciseQuery.addValueEventListener(exerciseQueryValueListener);

        goals = mGoalDatabase.child("goals");
        goals.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                goalCalories = 0;
                goalFat = 0;
                goalCarbs = 0;
                goalProtein = 0;

                if (snapshot.child(userID).exists())
                {
                    UserGoals userGoals = snapshot.child(userID).getValue(UserGoals.class);
                    goalCalories = userGoals.calorieGoal;
                    goalFat = userGoals.fatGoal;
                    goalCarbs = userGoals.carbGoal;
                    goalProtein = userGoals.proteinGoal;

                    setProgress();
                    setChart();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        //progressBarHandler = new Handler();

        setProgress();
        createListeners();
        setChart();


        return rootView;
    }

    public void setProgress(){
        caloriebar =  rootView.findViewById(R.id.calorieBar);
        carbbar = rootView.findViewById(R.id.carbBar);
        fatbar = rootView.findViewById(R.id.fatBar);
        proteinbar = rootView.findViewById(R.id.proteinBar);

        calorieView = (TextView) rootView.findViewById(R.id.calorieView);
        carbView = (TextView) rootView.findViewById(R.id.carbView);
        fatView = (TextView) rootView.findViewById(R.id.fatView);
        proteinView = (TextView) rootView.findViewById(R.id.proteinView);

        calorieView.setText((dailyCalGain - dailyCalBurn) + "/" + goalCalories);
        carbView.setText(dailyCarb + "/" + goalCarbs);
        fatView.setText(dailyFat + "/" + goalFat);
        proteinView.setText(dailyProtein + "/" + goalProtein);


                if (goalCalories != 0){
                    caloriebar.setMax(goalCalories);
                    caloriebar.setProgress((dailyCalGain - dailyCalBurn));
                }
                if (goalCarbs != 0) {
                    carbbar.setMax(goalCarbs);
                    carbbar.setProgress(dailyCarb);
                }
                if (goalFat != 0) {
                    fatbar.setMax(goalFat);
                    fatbar.setProgress(dailyFat);
                }
                if (goalProtein != 0) {
                    proteinbar.setMax(goalProtein);
                    proteinbar.setProgress(dailyProtein);
                }

    }

    public void setChart(){
        /*for (int i = 0; i < 7; i++){
            c = Calendar.getInstance();
            c.add(Calendar.DATE, -i);
            dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
            foodchartQuery = mFoodDatabase.child(dateString).orderByKey();
            exercisechartQuery = mExerciseDatabase.child(dateString).orderByKey();
            foodchartQuery.addValueEventListener(foodchartQueryValueListener);
            exercisechartQuery.addValueEventListener(exercisechartQueryValueListener);
            counter++;
        }
        counter = 0;*/

        c = Calendar.getInstance();
        dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        foodchartQuery = mFoodDatabase.child(dateString).orderByKey();
        exercisechartQuery = mExerciseDatabase.child(dateString).orderByKey();
        foodchartQuery.addValueEventListener(foodchartQueryValueListener);
        exercisechartQuery.addValueEventListener(exercisechartQueryValueListener);

        c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        foodchartQuery2 = mFoodDatabase.child(dateString).orderByKey();
        exercisechartQuery2 = mExerciseDatabase.child(dateString).orderByKey();
        foodchartQuery2.addValueEventListener(foodchartQueryValueListener2);
        exercisechartQuery2.addValueEventListener(exercisechartQueryValueListener2);

        c = Calendar.getInstance();
        c.add(Calendar.DATE, -2);
        dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        foodchartQuery3 = mFoodDatabase.child(dateString).orderByKey();
        exercisechartQuery3 = mExerciseDatabase.child(dateString).orderByKey();
        foodchartQuery3.addValueEventListener(foodchartQueryValueListener3);
        exercisechartQuery3.addValueEventListener(exercisechartQueryValueListener3);

        c = Calendar.getInstance();
        c.add(Calendar.DATE, -3);
        dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        foodchartQuery4 = mFoodDatabase.child(dateString).orderByKey();
        exercisechartQuery4 = mExerciseDatabase.child(dateString).orderByKey();
        foodchartQuery4.addValueEventListener(foodchartQueryValueListener4);
        exercisechartQuery4.addValueEventListener(exercisechartQueryValueListener4);

        c = Calendar.getInstance();
        c.add(Calendar.DATE, -4);
        dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        foodchartQuery5 = mFoodDatabase.child(dateString).orderByKey();
        exercisechartQuery5 = mExerciseDatabase.child(dateString).orderByKey();
        foodchartQuery5.addValueEventListener(foodchartQueryValueListener5);
        exercisechartQuery5.addValueEventListener(exercisechartQueryValueListener5);

        c = Calendar.getInstance();
        c.add(Calendar.DATE, -5);
        dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        foodchartQuery6 = mFoodDatabase.child(dateString).orderByKey();
        exercisechartQuery6 = mExerciseDatabase.child(dateString).orderByKey();
        foodchartQuery6.addValueEventListener(foodchartQueryValueListener6);
        exercisechartQuery6.addValueEventListener(exercisechartQueryValueListener6);

        c = Calendar.getInstance();
        c.add(Calendar.DATE, -6);
        dateString =  c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        foodchartQuery7 = mFoodDatabase.child(dateString).orderByKey();
        exercisechartQuery7 = mExerciseDatabase.child(dateString).orderByKey();
        foodchartQuery7.addValueEventListener(foodchartQueryValueListener7);
        exercisechartQuery7.addValueEventListener(exercisechartQueryValueListener7);

        chart = (LineChart) rootView.findViewById(R.id.weeklyChart);
        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("");
        chart.setDescription(description);

        List<Entry> calGainEntries = new ArrayList();
        calGainEntries.add(new Entry(0f, weeklyCaloriesGained[6]));
        calGainEntries.add(new Entry(1f, weeklyCaloriesGained[5]));
        calGainEntries.add(new Entry(2f, weeklyCaloriesGained[4]));
        calGainEntries.add(new Entry(3f, weeklyCaloriesGained[3]));
        calGainEntries.add(new Entry(4f, weeklyCaloriesGained[2]));
        calGainEntries.add(new Entry(5f, weeklyCaloriesGained[1]));
        calGainEntries.add(new Entry(6f, weeklyCaloriesGained[0]));

        List<Entry> calBurnEntries = new ArrayList();
        calBurnEntries.add(new Entry(0f, weeklyCaloriesBurned[6]));
        calBurnEntries.add(new Entry(1f, weeklyCaloriesBurned[5]));
        calBurnEntries.add(new Entry(2f, weeklyCaloriesBurned[4]));
        calBurnEntries.add(new Entry(3f, weeklyCaloriesBurned[3]));
        calBurnEntries.add(new Entry(4f, weeklyCaloriesBurned[2]));
        calBurnEntries.add(new Entry(5f, weeklyCaloriesBurned[1]));
        calBurnEntries.add(new Entry(6f, weeklyCaloriesBurned[0]));

        LineDataSet calGainDataset = new LineDataSet(calGainEntries, "Calories Gained");
        calGainDataset.setAxisDependency(AxisDependency.LEFT);
        calGainDataset.setColors(Color.rgb(106, 150, 31));
        LineDataSet calBurnDataset = new LineDataSet(calBurnEntries, "Calories Burned");
        calBurnDataset.setAxisDependency(AxisDependency.LEFT);
        calBurnDataset.setColors(Color.rgb(255, 102, 0));

        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(calGainDataset);
        dataSets.add(calBurnDataset);

        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.invalidate(); // refresh

        String[] labels = new String[7];

        for (int i = 0; i < 7; i++){
            c = Calendar.getInstance();
            c.add(Calendar.DATE, -i);
            String chartDateString =  (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
            labels[6 - i] = chartDateString;
        }
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
    }

    public void createListeners(){
        foodchartQueryValueListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                weeklyCaloriesGained[0] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);
                    weeklyCaloriesGained[0] += (int)food.calories;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        exercisechartQueryValueListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                weeklyCaloriesBurned[0] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Exercise exercise = next.getValue(Exercise.class);
                    weeklyCaloriesBurned[0] += exercise.caloriesBurned;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        foodchartQueryValueListener2 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                weeklyCaloriesGained[1] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);
                    weeklyCaloriesGained[1] += (int)food.calories;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        exercisechartQueryValueListener2 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                weeklyCaloriesBurned[1] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Exercise exercise = next.getValue(Exercise.class);
                    weeklyCaloriesBurned[1] += exercise.caloriesBurned;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        foodchartQueryValueListener3 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                weeklyCaloriesGained[2] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);
                    weeklyCaloriesGained[2] += (int)food.calories;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        exercisechartQueryValueListener3 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                weeklyCaloriesBurned[2] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Exercise exercise = next.getValue(Exercise.class);
                    weeklyCaloriesBurned[2] += exercise.caloriesBurned;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        foodchartQueryValueListener4 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                weeklyCaloriesGained[3] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);
                    weeklyCaloriesGained[3] += (int)food.calories;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        exercisechartQueryValueListener4 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                weeklyCaloriesBurned[3] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Exercise exercise = next.getValue(Exercise.class);
                    weeklyCaloriesBurned[3] += exercise.caloriesBurned;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        foodchartQueryValueListener5 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                weeklyCaloriesGained[4] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);
                    weeklyCaloriesGained[4] += (int)food.calories;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        exercisechartQueryValueListener5 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                weeklyCaloriesBurned[4] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Exercise exercise = next.getValue(Exercise.class);
                    weeklyCaloriesBurned[4] += exercise.caloriesBurned;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        foodchartQueryValueListener6 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                weeklyCaloriesGained[5] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);
                    weeklyCaloriesGained[5] += (int)food.calories;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        exercisechartQueryValueListener6 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                weeklyCaloriesBurned[5] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Exercise exercise = next.getValue(Exercise.class);
                    weeklyCaloriesBurned[5] += exercise.caloriesBurned;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        foodchartQueryValueListener7 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                weeklyCaloriesGained[6] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Food food = next.getValue(Food.class);
                    weeklyCaloriesGained[6] += (int)food.calories;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        exercisechartQueryValueListener7 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                weeklyCaloriesBurned[6] = 0;

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Exercise exercise = next.getValue(Exercise.class);
                    weeklyCaloriesBurned[6] += exercise.caloriesBurned;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

}
