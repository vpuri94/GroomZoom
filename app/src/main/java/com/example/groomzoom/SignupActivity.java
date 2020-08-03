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
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etEmail = (EditText) findViewById(R.id.etEmailAddress);
        btnAddress = (Button) findViewById(R.id.btnAddress);
        etPrice = (EditText) findViewById(R.id.editTextNumber);
        cbHaircut = findViewById(R.id.cbHaircut);
        cbBeard = findViewById(R.id.cbBeard);
        cbColor = findViewById(R.id.cbColor);
        cbWax = findViewById(R.id.cbWax);
        cbBlowdry = findViewById(R.id.cbBlowdry);
        btnPictures = findViewById(R.id.btnPictures);

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEnterAddress();
            }
        });

        if(!currentUser.getBoolean("barber"))
            etPrice.setVisibility(View.GONE);

        btnPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etEmail.getText().toString().isEmpty()){
                    Toasty.error(getApplicationContext(), "First input an email address!", Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                if(!etEmail.getText().toString().trim().matches(emailPattern)){
                    Toasty.error(getApplicationContext(), "Invalid email address!", Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                if(currentUser.getBoolean("barber")) {
                    if (etPrice.getText().toString().isEmpty()) {
                        Toasty.error(getApplicationContext(), "First input a price", Toasty.LENGTH_SHORT, true).show();
                        return;
                    }
                }
                if(currentUser.getParseGeoPoint("mapPoint") == null){
                    Toasty.error(getApplicationContext(), "First input a postal address!", Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                if(!cbHaircut.isChecked() && !cbBeard.isChecked() && !cbColor.isChecked() && !cbBlowdry.isChecked() && !cbWax.isChecked()){
                    Toasty.error(getApplicationContext(), "Must Select atleast one service!", Toasty.LENGTH_SHORT, true).show();
                    return;
                }
            }
        });

    }
    private void goEnterAddress(){
        Intent googleMaps = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(googleMaps, requestCodeNum);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeNum && resultCode == RESULT_OK) {
            String result = data.getStringExtra(addressKey);
            tvAddress.setText(result);
            myself.put(addressKey, result);
            myself.saveInBackground();
        }

    }
}