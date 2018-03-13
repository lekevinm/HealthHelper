package cs125.healthhelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FoodRecActivity extends AppCompatActivity {

    private static String API_ID = "6e77ea68";
    private static String API_KEY = "7bc379ae4bdaa6104685ddf9154352ca";
    ListView listView;
    ArrayList<FoodRecommendation> foodRecsMaster;
    private ArrayAdapter<FoodRecommendation> adapter;

    private String caloriesTarget;
    private String fatTarget;
    private String carbsTarget;
    private String proteinTarget;

    private FirebaseUser user;
    private String userID;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_rec);
        listView = (ListView)findViewById(R.id.food_rec_listview);

        foodRecsMaster = new ArrayList<FoodRecommendation>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getGoalTargets();

        //pair up the listview with an ArrayList<FoodRecommendation> by using an ArrayAdapter
        ArrayList<FoodRecommendation> foodlist = new ArrayList<FoodRecommendation>();
        adapter = new RecAdapter(foodlist, getApplicationContext());
        ListView listView = findViewById(R.id.food_rec_listview);

        FoodRecommendations foodRecommendation = new FoodRecommendations(fatTarget, carbsTarget, proteinTarget);
        foodRecommendation.setup(adapter);
        foodRecommendation.execute();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodRecommendation rec = (FoodRecommendation)parent.getItemAtPosition(position);
                Intent intent = new Intent(FoodRecActivity.this, FoodRecPopup.class);
                intent.putExtra("foodRec", rec);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getGoalTargets();
    }

    private void getGoalTargets() {
        DatabaseReference goals = mDatabase.child("goals");
        goals.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(userID).exists())
                {
                    UserGoals userGoals = snapshot.child(userID).getValue(UserGoals.class);
                    caloriesTarget = Integer.toString(userGoals.calorieGoal);
                    fatTarget = Integer.toString(userGoals.fatGoal);
                    carbsTarget = Integer.toString(userGoals.carbGoal);
                    proteinTarget = Integer.toString(userGoals.proteinGoal);
//                    Log.d("Calories: ", caloriesTarget);
//                    Log.d("Fat: ", fatTarget);
//                    Log.d("Carbs: ", carbsTarget);
//                    Log.d("Protein: ", proteinTarget);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    class FoodRecommendations extends AsyncTask<String, String, String> {
        private String API_ID = "6e77ea68";
        private String API_KEY = "7bc379ae4bdaa6104685ddf9154352ca";
        private ArrayAdapter<FoodRecommendation> array_adapter;
        private ArrayList<FoodRecommendation> foodRecs;
        private String fatTarget = "100";
        private String carbsTarget = "100";
        private String proteinTarget = "100";

        public FoodRecommendations(String fatTarget, String carbsTarget, String proteinTarget) {
            foodRecs = new ArrayList<FoodRecommendation>();
//            this.fatTarget = fatTarget;
//            this.carbsTarget = carbsTarget;
//            this.proteinTarget = proteinTarget;
        }
        public void setup(ArrayAdapter<FoodRecommendation> arr){
            array_adapter = arr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Log.d("Testing: ", "Getting Food Suggestions");
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                getFoodRecommendations(fatTarget, carbsTarget, proteinTarget);
            }
            catch (IOException e){
                System.out.println("getFoodRecommendation IOException occurred.");
            }
            catch (JSONException je) {
                System.out.println("getFoodRecommendation JSONException occurred");
                je.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s){
            array_adapter.clear();
            array_adapter.addAll(foodRecs); //add the new list of foods into the listview adapter
            array_adapter.notifyDataSetChanged();
        }

        public void getFoodRecommendations(String fatTarget, String carbsTarget, String proteinTarget) throws IOException, JSONException {
            String base_url = "http://api.yummly.com/v1/api/recipes?_app_id=" + API_ID + "&_app_key=" + API_KEY;
            String query_params = "&nutrition.FAT.max=" + fatTarget + "&nutrition.CHOCDF.max=" + carbsTarget +
                    "&nutrition.PROCNT.max=" + proteinTarget;
            String url = base_url + query_params;
//            Log.d("Yummly URL: ", url);
            String json = sendGetRequest(new URL(url));

            parseFoodList(json);
        }

        private void parseFoodList(String string) throws JSONException {
            try {
                JSONObject jObj = new JSONObject(string);
                if (jObj.names().get(0).equals("errors")) { //return empty foodlist if keyword matched nothing.
                    return;
                }
                JSONArray jArray = jObj.getJSONArray("matches");
                String recipeName = "";
                String ingredients = "";
                String course = "";
                String source = "";
                String totalTime = "";
                for (int i = 0; i < jArray.length(); i++) {
                    recipeName = jArray.getJSONObject(i).getString("recipeName");
//                    Log.d("Testing JSON: ", recipeName);
                    ingredients = jArray.getJSONObject(i).getString("ingredients");
//                    Log.d("Testing JSON: ", ingredients);
                    if (jArray.getJSONObject(i).has("attributes")) {
                        JSONObject attributes = jArray.getJSONObject(i).getJSONObject("attributes");
//                        Log.d("Testing JSON: ", "Attribute available.");
                        if (attributes.has("course")) {
                            course = attributes.get("course").toString();
//                            Log.d("Testing JSON: ", course);
                        }
                        else {
                            course = "";
                        }
                    }
                    source = jArray.getJSONObject(i).getString("sourceDisplayName");
//                    Log.d("Testing JSON: ", source);
                    totalTime = jArray.getJSONObject(i).getString("totalTimeInSeconds");
//                    Log.d("Testing JSON: ", totalTime);

//                    Log.d("Testing JSON: ", recipeName);
                    foodRecs.add(new FoodRecommendation(recipeName, ingredients, course, source, totalTime));
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                throw new JSONException("Food list JSON not in expected format");
            }
        }


        private String sendGetRequest(URL url) throws IOException {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            Log.d("Testing: ", "Opened URL connection");
            con.setRequestMethod("GET");
//            Log.d("Testing: ", "GET request method successful");
            con.setRequestProperty("User-Agent","Mozilla/5.0");
//            Log.d("Testing: ", "Set Request Property done");
            String responseMessage = con.getResponseMessage();
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                throw new IOException("GET response failed: " + responseCode);
            }
        }
    }

}
