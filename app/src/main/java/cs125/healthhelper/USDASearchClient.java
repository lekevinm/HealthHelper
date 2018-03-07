package cs125.healthhelper;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.*;


/**
 * Created by sean1 on 3/3/2018.
 */


/**
 * The USDASearchClient handles the network calls for search queries on the database
 * This is handled as an AsyncTask (running on a thread) as to not block the main UI thread
 * a new USDASearchClient object should be created, setup, and executed
 * the client will then call
 */
public class USDASearchClient extends AsyncTask<String, Void, String>{


	private static String APIKEY = "enKmwyWfbRT7dJStQfX4FtgBUTWaxctTti8tBpAd";
    private ArrayAdapter<Food> array_adapter;
    private ArrayList<Food> foodlist;


    public USDASearchClient(){
        //can later move setup into the constructor. But kept separate for now.
    }

    public void setup(ArrayAdapter<Food> aa){
        array_adapter = aa;
    }

	/**
     *
	 * Asynchronous methods implemented below
	 * doInBackground will run the network stuff
     * onPostExecute will run after network stuff finishes
	 *
	 */

	@Override
	protected String doInBackground(String... args) {
        try{
            foodlist = queryFood(args[0]);  //store the found Food items in a list
            if (foodlist.size() == 0){
                foodlist.add(new Food("No Matches Found",0));
            }
        }
        catch (IOException e) {
            //"Lost Connection...";
            e.printStackTrace();
        }
        catch (Exception e) {
            //"Unexpected Error...";
            e.printStackTrace();
        }
        return null;
    }

	@Override
	protected void onPostExecute(String s){
        array_adapter.clear();
        array_adapter.addAll(foodlist); //add the new list of foods into the listview adapter
        array_adapter.notifyDataSetChanged();
	}



	/**
	 *
	 * queryFood return a list of Food objects that match a keyword
     * will return an ArrayList of Food items, or null if the method fails.
     *
     */


    public ArrayList<Food> queryFood(String keyword) throws IOException, JSONException {

        String url1 = "https://api.nal.usda.gov/ndb/search/?format=json&q=";
        String url2 = "&sort=n&max=500&offset=0&api_key=";
        String url = url1 + URLEncoder.encode(keyword, "UTF-8") + url2 + APIKEY; //encodes the keyword search part
        String json = sendGetRequest(new URL(url));

        ArrayList<Food> fl = parseFoodList(json);
        return fl;
    }


    /**
     *
     *
     * HELPER METHODS BELOW
	 *
	 *
	 */

    
    private ArrayList<Food> parseFoodList(String string) throws JSONException {
    	try {
	    	ArrayList<Food> foodlist = new ArrayList<Food>();
	    	
	    	JSONObject jObj = new JSONObject(string);
	    	if (jObj.names().get(0).equals("errors")) { //return empty foodlist if keyword matched nothing.
	    		return foodlist;
	    	}
			JSONArray jArray = jObj.getJSONObject("list").getJSONArray("item");
			String item;
			int id; 
			for (int i = 0; i < jArray.length(); i++) {
				item = ((JSONObject) jArray.get(i)).get("name").toString();
				item = item.split("UPC:")[0];
				//System.out.println(item);
				id = Integer.parseInt(((JSONObject) jArray.get(i)).get("ndbno").toString());
				//System.out.println(id);
				
				foodlist.add(new Food(item,id));
			}
    	return foodlist;
    	}
    	catch (JSONException e) {
    		throw new JSONException("Food list JSON not in expected format");
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

