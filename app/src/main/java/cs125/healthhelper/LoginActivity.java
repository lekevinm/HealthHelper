package cs125.healthhelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        // after verification, go to MainActivity
        Intent createIntent = new Intent(this, MainActivity.class);

        startActivity(createIntent);
    }

    public void createAccount(View view){
        Intent createIntent = new Intent(this, AccountActivity.class);

        startActivity(createIntent);
    }

}
