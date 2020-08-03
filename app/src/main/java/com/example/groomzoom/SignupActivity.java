package com.example.groomzoom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
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
    String emailKey = "email";
    String priceKey = "price";
    String geokey = "mapPoint";


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

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEnterAddress();
            }
        });

        if(!currentUser.getBoolean(barberKey))
            etPrice.setVisibility(View.GONE);

        btnPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etEmail.getText().toString().isEmpty()){
                    Toasty.error(getApplicationContext(), noEmailMsg, Toasty.LENGTH_SHORT, true).show();
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
                currentUser.put(priceKey, Integer.valueOf(etPrice.getText().toString()));
                serviceChanges(view);
                currentUser.saveInBackground();
                goMainActivity();
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