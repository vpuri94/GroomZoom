package com.example.groomzoom;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import org.parceler.Parcels;
import java.util.List;

public class AppointmentDetailsActivity extends AppCompatActivity {
    // the appointment to display
    Appointments appointment;
    public static String appointmentMsg = "Appointment with: ";
    public static String dateMsg = "Date of appt: ";
    boolean isBarber;
    ImageView ivProfilepic;
    public String priceMsg = "Price of appt. : $";
    RatingBar rbAppt;
    TextView tvApptname;
    TextView tvDate;
    TextView tvPrice;
    TextView tvServicesList;
    String barberKey = "barber";
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            ivProfilepic.setScaleX(mScaleFactor);
            ivProfilepic.setScaleY(mScaleFactor);
            return true;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        tvApptname = (TextView) findViewById(R.id.tvApptname);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDate = (TextView) findViewById(R.id.tvDate);
        ivProfilepic = (ImageView) findViewById(R.id.ivProfilepic);
        rbAppt = (RatingBar) findViewById(R.id.rbAppt);
        tvServicesList  = (TextView) findViewById(R.id.tvServicesList);
        // unwrap the appointment passed in via intent, using its simple name as a key
        appointment = (Appointments) Parcels.unwrap(getIntent().getParcelableExtra(Appointments.class.getSimpleName()));
        // set appointment name and price and date
        isBarber = ParseUser.getCurrentUser().getBoolean(barberKey);
        if(isBarber) {
            try {
                tvApptname.setText(appointmentMsg + appointment.getBooker().fetchIfNeeded().getUsername());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                tvApptname.setText(appointmentMsg + appointment.getUser().fetchIfNeeded().getUsername());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        tvPrice.setText(priceMsg + appointment.getPrice());
        tvDate.setText(dateMsg + appointment.getDate()[0] + " @ " + appointment.getDate()[1]);
        ParseFile image;
        if(isBarber)
            image = appointment.getProfilePic();
        else
            image = appointment.getBarberProfilePic();
        if (image != null) {
            Glide.with(getApplicationContext()).load(image.getUrl()).into(ivProfilepic);
        }

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        float rating = (float) appointment.getRating();
        rbAppt.setRating(rating = rating > 0 ? rating / 2.0f : rating);
        tvServicesList.setText(bulletedVersion(appointment.getServices()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;


    }

    public String bulletedVersion(List<String> listOfServices){
        String bulletedConcatCopy = "";
        for(int x = 0; x < listOfServices.size(); x++){
            bulletedConcatCopy += "\t • " + listOfServices.get(x) + "\n";
        }
        return bulletedConcatCopy;
    }

}
