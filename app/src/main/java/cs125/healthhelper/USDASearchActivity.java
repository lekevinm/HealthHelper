package cs125.healthhelper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class USDASearchActivity extends AppCompatActivity {

    private EditText search;
    private ArrayAdapter<Food> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usdasearch);
        search = findViewById(R.id.searchQuery);

        //pair up the listview with an ArrayList<Food> by using an ArrayAdapter
        ArrayList<Food> foodlist = new ArrayList<Food>();
        adapter = new ArrayAdapter<Food>(this, R.layout.food_listview, foodlist);
        ListView listView = findViewById(R.id.food_listview);
        listView.setAdapter(adapter);
    }

    //passes the search query into the USDA client, and updates the listview.
    public void search(View view){
        String keyword = search.getText().toString();
        USDASearchClient client = new USDASearchClient();
        client.setup(adapter);
        client.execute(keyword);
    }


}
