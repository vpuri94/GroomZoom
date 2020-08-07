package com.example.groomzoom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnAddress;
    private EditText etPrice;
    private Button btnPictures;
    private CheckBox cbHaircut;
    private CheckBox cbColor;
    private CheckBox cbBeard;
    private CheckBox cbWax;
    private CheckBox cbBlowdry;
    private EditText etPhone;
    private EditText etName;
    public static final int requestCodeNum = 999;
    public static final int requestCodeNum2 = 1000;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ParseUser currentUser = ParseUser.getCurrentUser();
    private String servicesKey = "services";
    private String addressKey = "address";
    private static final int RESULT_OK = 23;
    private String signupSuccess = "Signed up successfully!";
    String barberKey = "barber";
    String noEmailMsg = "First input an email address!";
    String invalidEmailMsg = "Invalid email address!";
    String invalidPriceMsg = "First input a price";
    String noAddressMsg = "First input a postal address!";
    String serviceMsg = "Must Select atleast one service!";
    String noNameMsg = "Must enter a first and last name!";
    String emailKey = "email";
    String priceKey = "price";
    String geokey = "mapPoint";
    String usernameKey = "username";
    String profilePicKey = "profilePic";
    String nameKey = "name";
    String ratingKey = "rating";
    String phoneKey = "phoneNum";
    String phoneMsg = "Please enter a valid phone number!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etEmail = (EditText) findViewById(R.id.etEmailAddress);
        btnAddress = (Button) findViewById(R.id.btnAddress);
        etPrice = (EditText) findViewById(R.id.editTextNumber);
        cbHaircut = (CheckBox) findViewById(R.id.cbHaircut2);
        cbBeard = (CheckBox) findViewById(R.id.cbBeard2);
        cbColor = (CheckBox)findViewById(R.id.cbColor2);
        cbWax = (CheckBox)findViewById(R.id.cbWax2);
        cbBlowdry = (CheckBox) findViewById(R.id.cbBlowdry2);
        btnPictures = findViewById(R.id.btnPictures);
        etPhone = findViewById(R.id.editTextPhone);
        etName = findViewById(R.id.etName);

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEnterAddress();
            }
        });

        if(!currentUser.getBoolean(barberKey)) {
            etPrice.setVisibility(View.GONE);
            etPhone.setVisibility(View.GONE);
        }

        btnPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etEmail.getText().toString().isEmpty()){
                    Toasty.error(getApplicationContext(), noEmailMsg, Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                if(etName.getText().toString().isEmpty()){
                    Toasty.error(getApplicationContext(), noNameMsg, Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                if(!etEmail.getText().toString().trim().matches(emailPattern)){
                    Toasty.error(getApplicationContext(), invalidEmailMsg, Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                if(currentUser.getBoolean(barberKey)) {
                    if (etPrice.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), invalidPriceMsg, Toasty.LENGTH_SHORT, true).show();
                        return;
                    }
                    if(etPhone.getText().toString().isEmpty() || etPhone.getText().toString().length() != 10){
                        Toasty.error(getApplicationContext(), phoneMsg, Toasty.LENGTH_SHORT, true).show();
                        return;
                    }
                }
                if(currentUser.getParseGeoPoint(geokey) == null){
                    Toasty.error(getApplicationContext(), noAddressMsg, Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                if(!(cbHaircut.isChecked()) && !(cbBeard.isChecked()) && !(cbColor.isChecked()) && !(cbBlowdry.isChecked()) && !(cbWax.isChecked())){
                    Toasty.error(getApplicationContext(), serviceMsg, Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                currentUser.put(emailKey, etEmail.getText().toString());
                if(currentUser.getBoolean(barberKey)) {
                    currentUser.put(priceKey, Integer.valueOf(etPrice.getText().toString()));
                    currentUser.put(phoneKey, etPhone.getText().toString());
                }
                currentUser.put(nameKey, etName.getText().toString());
                serviceChanges(view);
                currentUser.saveInBackground();
                createNewBrowseObject();
                goMainActivity();
            }
        });

    }

    private void createNewBrowseObject() {
        ParseObject obj = new Browse();
        obj.put(profilePicKey, currentUser.getParseFile(profilePicKey));
        obj.put(usernameKey, currentUser.getString(usernameKey));
        obj.put(nameKey, currentUser.getString(nameKey));
        obj.put(barberKey, currentUser.getBoolean(barberKey));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
        query.whereEqualTo("user", currentUser);
        ParseObject ratingObj = null;
        try {
            ratingObj =  query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        obj.put(ratingKey, ratingObj.getNumber("ratingSum").floatValue() / ratingObj.getNumber("numRatings").floatValue());
        obj.put(servicesKey, currentUser.getList(servicesKey));
        obj.put(addressKey, currentUser);
        obj.put(priceKey, currentUser.getNumber(priceKey));
        obj.put(phoneKey, currentUser.getString(phoneKey));
        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                } else {
                    e.printStackTrace();

                }
            }
        });
    }


    private void goEnterAddress(){
        Intent googleMaps = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(googleMaps, requestCodeNum);
    }

    private void goMainActivity() {
        Intent goToMain  = new Intent(this, MainActivity.class);
        startActivity(goToMain);
        Toasty.success(SignupActivity.this, signupSuccess, Toasty.LENGTH_SHORT, true).show();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == requestCodeNum && resultCode == RESULT_OK) {
            String result = data.getStringExtra(addressKey);
            btnAddress.setText(result);
            currentUser.put(addressKey, result);
            currentUser.saveInBackground();
        }
        if (requestCode == requestCodeNum && resultCode == RESULT_OK) {
            String result = data.getStringExtra(addressKey);
            btnAddress.setText(result);
            currentUser.put(addressKey, result);
            currentUser.saveInBackground();
        }
    }

    public void serviceChanges(View v){
        // go through each service checkbox, and if its checked, add it to the serviceList of offered services
        List<String> serviceList = new ArrayList<String>();
        if(cbHaircut.isChecked())
            serviceList.add(String.valueOf(cbHaircut.getText()));
        if(cbBeard.isChecked())
            serviceList.add(String.valueOf(cbBeard.getText()));
        if(cbColor.isChecked())
            serviceList.add(String.valueOf(cbColor.getText()));
        if(cbWax.isChecked())
            serviceList.add(String.valueOf(cbWax.getText()));
        if(cbBlowdry.isChecked())
            serviceList.add(String.valueOf(cbBlowdry.getText()));
        currentUser.put(servicesKey, serviceList);
        currentUser.saveInBackground();
    }
}