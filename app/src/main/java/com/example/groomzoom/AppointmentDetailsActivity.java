package com.example.groomzoom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.parceler.Parcels;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AppointmentDetailsActivity extends AppCompatActivity {
    // the appointment to display
    Appointments appointment;
    public static String appointmentMsg = "Appointment with: ";
    public static String dateMsg = "Appt is @: ";
    boolean isBarber;
    ImageView ivProfilepic;
    ImageView ivLeftPic;
    ImageView ivRightPic;
    public String priceMsg = "Price of appt. : $";
    MaterialRatingBar rbAppt;
    MaterialRatingBar rbYourRating;
    TextView tvApptname;
    TextView tvDate;
    TextView tvPrice;
    TextView tvServicesList;
    String barberKey = "barber";
    String phoneNumKey = "phoneNum";
    String smsFormat = "smsto:";
    String userKey = "user";
    String numRatingKey = "numRatings";
    String ratingSumKey = "ratingSum";
    String ratedKey = "rated";
    Button phoneButton;
    ParseUser currentUser =  ParseUser.getCurrentUser();
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            // Pinch to zoom feature
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            // Scales the profile picture by the factor above
            ivProfilepic.setScaleX(mScaleFactor);
            ivProfilepic.setScaleY(mScaleFactor);
            ivLeftPic.setScaleX(mScaleFactor);
            ivLeftPic.setScaleY(mScaleFactor);
            ivRightPic.setScaleX(mScaleFactor);
            ivRightPic.setScaleY(mScaleFactor);
            return true;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize viws
        setContentView(R.layout.activity_appointment_details);
        tvApptname = (TextView) findViewById(R.id.tvApptname);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDate = (TextView) findViewById(R.id.tvDate);
        ivProfilepic = (ImageView) findViewById(R.id.ivProfilepic);
        ivLeftPic = (ImageView) findViewById(R.id.ivLeftPic);
        ivRightPic = (ImageView) findViewById(R.id.ivRightPic);
        rbAppt = (MaterialRatingBar) findViewById(R.id.rbAppt);
        rbYourRating = (MaterialRatingBar) findViewById(R.id.rbYourRating);

        tvServicesList  = (TextView) findViewById(R.id.tvServicesList);
        // unwrap the appointment passed in via intent, using its simple name as a key
        appointment = (Appointments) Parcels.unwrap(getIntent().getParcelableExtra(Appointments.class.getSimpleName()));
        // set appointment name and price and date
        isBarber = currentUser.getBoolean(barberKey);
        phoneButton = (Button) findViewById(R.id.btnPhone);

        // display message with other user, casing on if barber or not
        if(isBarber) {
            try {
                // use fetchifneeded if parse user has not been fetchd
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
        ParseFile leftImage = null;
        ParseFile rightImage = null;
        try {
            leftImage = appointment.getBooker().fetchIfNeeded().getParseFile("leftSelfie");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            rightImage = appointment.getBooker().fetchIfNeeded().getParseFile("rightSelfie");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(isBarber) {
            image = appointment.getProfilePic();
            phoneButton.setVisibility(View.GONE);
        }
        else {
            image = appointment.getBarberProfilePic();
            // get phone number from parsebackend
            String phoneNumber = appointment.getString(phoneNumKey);
            Toasty.success(getApplicationContext(), "Phone num is "+phoneNumber, Toasty.LENGTH_SHORT, true).show();
            final String smsNumber  = smsFormat + phoneNumber;
            // send text as implicit intent
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setData(Uri.parse(smsNumber));
                    if (smsIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(smsIntent);
                    } else {
                        return;
                    }
                }
            });
        }
        if (image != null) {
            Glide.with(getApplicationContext()).load(image.getUrl()).into(ivProfilepic);
        }
        if(leftImage != null)
            Glide.with(getApplicationContext()).load(leftImage.getUrl()).into(ivLeftPic);
        if(rightImage != null)
            Glide.with(getApplicationContext()).load(rightImage.getUrl()).into(ivRightPic);

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ratings");
        if(isBarber)
            query.whereEqualTo(userKey, appointment.getBooker());
        else
            query.whereEqualTo(userKey, appointment.getUser());
        ParseObject ratingObj = null;
        try {
            ratingObj =  query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // display rating of barber from parse server from 0-5, in increments of 0.5
        float rating = ratingObj.getNumber(ratingSumKey).floatValue()  / ratingObj.getNumber(numRatingKey).floatValue();
        rbAppt.setRating(rating);

        if(appointment.getBoolean(ratedKey)) {
            rbYourRating.setRating((float) ((double) (ratingObj.getNumber(ratingSumKey).doubleValue() / ratingObj.getNumber(numRatingKey).doubleValue())));
            rbYourRating.setIsIndicator(true);
        }

        ParseObject finalRatingObj = ratingObj;
        rbYourRating.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                if(appointment.getBoolean(ratedKey))
                    return;
                else {
                    rbYourRating.setIsIndicator(true);
                    appointment.put(ratedKey, true);
                    appointment.saveInBackground();
                    int numRatings = 0;
                    float ratingFrom = (float) 0.0;
                    numRatings = finalRatingObj.getNumber(numRatingKey).intValue();
                    ratingFrom =  (finalRatingObj.getNumber(ratingSumKey).floatValue()) / (finalRatingObj.getNumber(numRatingKey).floatValue());
                    float newRating = (finalRatingObj.getNumber(ratingSumKey).floatValue() + rating);
                    finalRatingObj.put(ratingSumKey, (Number) newRating);
                    finalRatingObj.put(numRatingKey, (Number)(numRatings + 1));
                    appointment.saveInBackground();
                    finalRatingObj.saveInBackground();
                    appointment.getBooker().saveInBackground();
                    appointment.getUser().saveInBackground();
                    appointment.saveInBackground();
                    Toasty.success(getApplicationContext(), ratedKey, Toasty.LENGTH_SHORT, true).show();
                }
                }
        });
        tvServicesList.setText(bulletedVersion(appointment.getServices()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;


    }

    // string manipulation to display a preview of list of services
    public String bulletedVersion(List<String> listOfServices){
        String bulletedConcatCopy = "";
        for(int x = 0; x < listOfServices.size(); x++){
            bulletedConcatCopy += "\t • " + listOfServices.get(x) + "\n";
        }
        return bulletedConcatCopy;
    }

}
