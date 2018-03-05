package cs125.healthhelper;

import android.support.annotation.NonNull;
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

import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "AccountActivity";

    private String email;
    private String password;
    private String password2;
    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText password2EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        password2EditText = findViewById(R.id.password2);

        TextView warningMessage = findViewById(R.id.warningMessage);
        warningMessage.setVisibility(View.GONE);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            Intent createIntent = new Intent(this, MainActivity.class);
            startActivity(createIntent);
        }
    }

    public void createAccount(View view){
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        password2 = password2EditText.getText().toString();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (password.equals(password2)) // checks if both passwords match
        {
            signup(email, password);
        }
        else // if both passwords don't match, return message
        {
            TextView warningMessage = findViewById(R.id.warningMessage);
            warningMessage.setVisibility(View.VISIBLE);
        }
        // after creation, go to MainActivity
    }

    public void signup(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(AccountActivity.this, MainActivity.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
