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

public class CalendarActivity extends AppCompatActivity {
private CalendarView mCalendarView;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int month, int year, int day) {
                String date = (year + 1) + "/" + day + "/" + month;
                SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
                Date selectedDate = null;
                try {
                    selectedDate = formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date currDate = new Date();
                String currDateStr = formatter.format(currDate);
                try {
                    currDate = formatter.parse(currDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(selectedDate.compareTo(currDate) < 0){
                    Toast.makeText(getApplicationContext(), "Must pick a date in the future!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(CalendarActivity.this, Booking.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }
}
