package com.example.groomzoom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GalleryActivity extends AppCompatActivity {

    ImageButton btnLeftPic;
    ImageButton btnFrontPic;
    ImageButton btnRightPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        btnFrontPic = (ImageButton) findViewById(R.id.btnFrontPic);
        btnLeftPic = (ImageButton) findViewById(R.id.btnLeftPic);
        btnRightPic = (ImageButton) findViewById(R.id.btnRightPic);
        btnFrontPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraActivity();
            }
        });
    }

    private void launchCameraActivity() {
        Intent changePictures = new Intent(getApplicationContext(), CustomCamera.class);
        startActivity(changePictures);
    }

}