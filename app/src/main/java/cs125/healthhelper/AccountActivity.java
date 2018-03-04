package cs125.healthhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;
import android.util.Log;

public class AccountActivity extends AppCompatActivity {

    private String username;
    private String password;
    private String password2;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText password2EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        password2EditText = findViewById(R.id.password2);

        TextView warningMessage = findViewById(R.id.warningMessage);
        warningMessage.setVisibility(View.GONE);
    }

    public void createAccount(View view){
        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        password2 = password2EditText.getText().toString();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (password.equals(password2)) // checks if both passwords match
        {
            UserLogin userInfo = new UserLogin(username, password);
            String userID = mDatabase.child("users").push().getKey(); // creates unique user id for database
            mDatabase.child("users").child(userID).setValue(userInfo); // stores username/password under userID in database

            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            // session.setUsername(AccountActivity.this, test.username);
            // session.setUserID(AccountActivity.this, userID);
        }
        else
        {
            TextView warningMessage = findViewById(R.id.warningMessage);
            warningMessage.setVisibility(View.VISIBLE);
            // print
        }
        // after creation, go to MainActivity
    }
}
