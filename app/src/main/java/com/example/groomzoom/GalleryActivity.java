package com.example.groomzoom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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
    TextView tvFront;
    TextView tvLeft;
    TextView tvRight;

    String frontKey = "front";
    String rightKey = "right";
    String leftKey = "left";
    String pictureMsg = "File found was";
    String pictureKey = "picture";
    String directionKey = "direction";
    String barberKey = "barber";
    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        btnFrontPic = (ImageButton) findViewById(R.id.btnFrontPic);
        btnLeftPic = (ImageButton) findViewById(R.id.btnLeftPic);
        btnRightPic = (ImageButton) findViewById(R.id.btnRightPic);
        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvFront = (TextView) findViewById(R.id.tvFront);
        tvRight = (TextView) findViewById(R.id.tvRight);

        btnFrontPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraActivity(frontKey);
            }
        });

        if(currentUser.getBoolean(barberKey)){
            btnLeftPic.setVisibility(View.GONE);
            btnRightPic.setVisibility(View.GONE);
            tvLeft.setVisibility(View.GONE);
            tvRight.setVisibility(View.GONE);
        }
        else {
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
    }

    private void launchCameraActivity(String key) {
        Intent changePictures = new Intent(getApplicationContext(), CustomCamera.class);
        changePictures.putExtra(directionKey, key);
        startActivityForResult(changePictures, 69);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69 && resultCode == RESULT_OK && data != null) {
            File pictureRetrieved = (File) data.getSerializableExtra(pictureKey);
            Toast.makeText(getApplicationContext(), pictureMsg + pictureRetrieved.getName(), Toast.LENGTH_LONG).show();
        }

    }
}