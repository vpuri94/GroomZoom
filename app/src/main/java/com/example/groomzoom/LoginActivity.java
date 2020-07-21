package com.example.groomzoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import static android.R.*;


public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;
    private String[] users = {"SELECT ACCOUNT TYPE","client", "barber"};
    private String optionSelected= "";
    private String signupError = "Need to select account type!";
    private String signupSuccess = "Signed up successfully!";
    private String loginSuccess = "Success!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(ParseUser.getCurrentUser() != null)
            goMainActivity();
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        // Creating the arrayadapter instance of the user list
        ArrayAdapter aa = new ArrayAdapter(this, layout.simple_spinner_item, users);
        aa.setDropDownViewResource(layout.simple_spinner_dropdown_item);
        // Setting the arrayadapter data on the spinner
        spin.setAdapter(aa);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(optionSelected == ""){
                    Toast.makeText(LoginActivity.this, signupError, Toast.LENGTH_SHORT).show();
                    return;
                }
                signUp(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    private void signUp(String newName, String newPword) {
        // Create new ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(newName);
        user.setPassword(newPword);
        if (optionSelected == users[2]) {
            user.put(users[2], true);
        }
        else {
            user.put(users[2], false);
        }
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    return;
                }
                else{
                    // Sign up worked
                    goMainActivity();
                    Toast.makeText(LoginActivity.this, signupSuccess, Toast.LENGTH_SHORT).show();
                }
            }
        });
        user.saveInBackground();
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    return;
                }
                else {
                    // Main activity if user has signed in properly
                    goMainActivity();
                    Toast.makeText(LoginActivity.this, loginSuccess, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goMainActivity() {
        Intent goToMain  = new Intent(this, MainActivity.class);
        startActivity(goToMain);
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i == 0){
            optionSelected = "";
        }
        else if(i == 1){
            optionSelected = users[1];
        }
        else{
            optionSelected = users[2];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        return;
    }
}
