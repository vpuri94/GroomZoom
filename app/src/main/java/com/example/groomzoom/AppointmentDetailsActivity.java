package com.example.groomzoom;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class AppointmentDetailsActivity extends AppCompatActivity {
    // the appointment to display
    Appointments appointment;
    TextView tvApptname;
    TextView tvPrice;
    TextView tvDate;
    ImageView ivProfilepic;
    RatingBar rbAppt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        tvApptname = (TextView) findViewById(R.id.tvApptname);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDate = (TextView) findViewById(R.id.tvDate);
        ivProfilepic = (ImageView) findViewById(R.id.ivProfilepic);
        rbAppt = (RatingBar) findViewById(R.id.rbAppt);
        // unwrap the appointment passed in via intent, using its simple name as a key
        appointment = (Appointments) Parcels.unwrap(getIntent().getParcelableExtra(Appointments.class.getSimpleName()));
        // set appointment name and price and date
        tvApptname.setText("Appointment with: " + appointment.getBooker());
        tvPrice.setText("Price of appt. : $" + appointment.getPrice());
        tvDate.setText("Date of appt: " + appointment.getDate()[0] + " @ " + appointment.getDate()[1]);
        ParseFile image = appointment.getProfilePic();
        if (image != null) {
            Glide.with(getApplicationContext()).load(image.getUrl()).into(ivProfilepic);
        }
        float rating = (float) appointment.getRating();
        rbAppt.setRating(rating = rating > 0 ? rating / 2.0f : rating);
    }
}
