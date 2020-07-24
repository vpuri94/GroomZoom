package com.example.groomzoom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {

    ImageButton btnLeftPic;
    ImageButton btnFrontPic;
    ImageButton btnRightPic;
    String frontKey = "front";
    String rightKey = "right";
    String leftKey = "left";

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
                launchCameraActivity(frontKey);
            }
        });

        btnLeftPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraActivity(leftKey);
            }
        });

        btnRightPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraActivity(rightKey);
            }
        });

    }

    private void launchCameraActivity(String key) {
        Intent changePictures = new Intent(getApplicationContext(), CustomCamera.class);
        changePictures.putExtra("direction", key);
        Log.i("gallery activity", key);
        startActivityForResult(changePictures, 69);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69 && resultCode == RESULT_OK && data != null) {
            File pictureRetrieved = (File) data.getSerializableExtra("picture");
            Toast.makeText(getApplicationContext(), "File found was" + pictureRetrieved.getName(), Toast.LENGTH_LONG).show();
        }

    }
}