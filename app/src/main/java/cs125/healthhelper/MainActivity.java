package cs125.healthhelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setupNavigationView();
    }

    // UI Code taken from Tutorialwing
    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if (bottomNavigationView != null) {
            
            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));
            
            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                                                                     new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    selectFragment(item);
                    return false;
                }
            });
        }
    }

    protected void selectFragment(MenuItem item) {
        
        item.setChecked(true);
        
        switch (item.getItemId()) {
            case R.id.navigation_home:
                pushFragment(new HomeFragment());
                break;
            case R.id.navigation_dashboard:
                pushFragment(new DashFragment());
                break;
            case R.id.navigation_notifications:
                pushFragment(new NotifFragment());
                break;
        }
    }

    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;
        
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }


}
