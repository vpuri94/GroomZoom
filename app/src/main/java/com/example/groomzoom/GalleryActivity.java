package com.example.groomzoom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    }
}