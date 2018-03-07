package cs125.healthhelper;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class USDASearchActivity extends AppCompatActivity {

    private EditText search;
    private ArrayAdapter<Food> adapter;
    private Button search_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usdasearch);
        search = findViewById(R.id.searchQuery);
        search_button = findViewById(R.id.searchButton);

        //pair up the listview with an ArrayList<Food> by using an ArrayAdapter
        ArrayList<Food> foodlist = new ArrayList<Food>();
        adapter = new ArrayAdapter<Food>(this, R.layout.food_listview, foodlist);
        ListView listView = findViewById(R.id.food_listview);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long l){
                Food food = (Food)adapter.getItemAtPosition(position);
                Intent intent = new Intent(getApplication(), USDAEntryActivity.class);
                intent.putExtra("TheFood",food);
                startActivity(intent);
            }
        });    }

    //passes the search query into the USDA client, and updates the listview.
    public void search(View view){
        search_button.setText("Searching");
        String keyword = search.getText().toString();
        USDASearchClient client = new USDASearchClient();
        client.setup(adapter,search_button);
        client.execute(keyword);
    }






}
