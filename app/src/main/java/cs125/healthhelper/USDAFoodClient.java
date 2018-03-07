package cs125.healthhelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.*;
import org.w3c.dom.Text;


/**
 * Created by sean1 on 3/3/2018.
 */

public class USDAFoodClient extends AsyncTask<String, Void, String>{


    private static String APIKEY = "enKmwyWfbRT7dJStQfX4FtgBUTWaxctTti8tBpAd";
    private Food food;
    private USDAEntryActivity activity;

    public USDAFoodClient(Food f, USDAEntryActivity a){
        food = f;
        activity = a;
    }

    /**
     * Asynchronous methods implemented below
     */


    @Override
    protected String doInBackground(String... args) {
        try {
            updateNutrients(food);
        }
        catch(Exception e){

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s){
        activity.gotInfo = true;
        activity.loadFoodInfo();

    }


    /**
     * setNutrients sets the nutritional value of a Food object
     */


    public boolean updateNutrients(Food food) throws IOException, JSONException {

        String url1 = "https://api.nal.usda.gov/ndb/reports/?ndbno=";
        String url2 = "&type=b&formatjson&api_key=";
        String url = url1 + food.ndbno + url2 + APIKEY;
        String json = sendGetRequest(new URL(url));

        //setNutrients of the Food object
        parseFoodNutrients(json,food);

        return true;
    }


    /**
     * helper methods below
     *
     *
     */




    private void parseFoodNutrients(String json, Food food) throws JSONException {
        try {
            JSONObject jObj = new JSONObject(json);
            JSONArray nut = (JSONArray) ((JSONObject) ((JSONObject) jObj.get("report")).get("food")).get("nutrients");
            for (int i = 0; i < nut.length(); i++) {
                int nutrient_id = Integer.parseInt(((JSONObject) nut.get(i)).get("nutrient_id").toString());

                switch (nutrient_id) {
                    case 208:
                        String cal = ((JSONObject) nut.get(i)).get("value").toString();
                        food.setBcals(Float.parseFloat(cal));
                        break;
                    case 205:
                        String car = ((JSONObject) nut.get(i)).get("value").toString();
                        food.setBcarbs(Float.parseFloat(car));
                        break;
                    case 203:
                        String pro = ((JSONObject) nut.get(i)).get("value").toString();
                        food.setBprotein(Float.parseFloat(pro));
                        break;
                    case 204:
                        String fat = ((JSONObject) nut.get(i)).get("value").toString();
                        food.setBfat(Float.parseFloat(fat));
                        break;
                }
            }
        }
        catch (JSONException e) {
            throw new JSONException("Food JSON not in expected format");
        }
    }


    private String sendGetRequest(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent","Mozilla/5.0");
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

