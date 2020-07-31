package com.example.groomzoom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CalendarActivity extends AppCompatActivity {
private CalendarView mCalendarView;
private String futureError = "Must pick a date in the future!";
private String datePattern = "M/dd/yyyy";
private String dateKey = "date";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            // called whenever a new date is selected from the calendar view
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int month, int year, int day) {
                String date = (year + 1) + "/" + day + "/" + month;
                SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
                Date selectedDate = null;
                try {
                    // save the selected date in the M/dd/yyyy pattern
                    selectedDate = formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date currDate = new Date();
                String currDateStr = formatter.format(currDate);
                try {
                    // save the current date in the M/dd/yyyy pattern
                    currDate = formatter.parse(currDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //and then compare the two dates
                // if you select a date in the past, give back an error message
                if(selectedDate.compareTo(currDate) < 0){
                    Toasty.error(getApplicationContext(), futureError, Toasty.LENGTH_SHORT, true).show();
                    return;
                }

                Intent incomingIntent = getIntent();
                final String objectId = incomingIntent.getStringExtra("objectId");

                Intent intent = new Intent(CalendarActivity.this, Booking.class);
                intent.putExtra(dateKey, date);
                intent.putExtra("id", objectId);
                startActivity(intent);
            }
        });
    }
}
