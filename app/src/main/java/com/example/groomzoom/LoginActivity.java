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
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import es.dmoral.toasty.Toasty;

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
                // if you sign up without specifying barber or client, give back an error
                if(optionSelected == ""){
                    Toasty.error(LoginActivity.this, signupError, Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                // otherwise try and sign them up
                signUp(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    private void signUp(String newName, String newPword) {
        // Create new ParseUser
        ParseUser user = new ParseUser();
        ParseACL postACL = new ParseACL(user);
        // allow ability to read/write to another user so you can modify their appointment list
        postACL.setPublicWriteAccess(true);
        user.setACL(postACL);
        // Set core properties
        user.setUsername(newName);
        user.setPassword(newPword);
        // store if the user is a barber or not
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
                    Toasty.success(LoginActivity.this, signupSuccess, Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
        user.saveInBackground();
    }

    // login the parse user with their credentials in the background thread
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
                    Toasty.success(LoginActivity.this, loginSuccess, Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void goMainActivity() {
        Intent goToMain  = new Intent(this, MainActivity.class);
        startActivity(goToMain);
        finish();
    }


    // sets variable of what kind of user the person is signing up as
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
