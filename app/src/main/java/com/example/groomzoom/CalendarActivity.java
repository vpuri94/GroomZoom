package com.example.groomzoom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
private CalendarView mCalendarView;

public class appointment{
    public String date;
    public int time;
    public appointment(String date, int time){
        this.date = date;
        this.time = time;
    }

    public String getDate(){
        return date;
    }

}


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int month, int year, int day) {
                String date = (year + 1) + "/" + day + "/" + month;
                Intent intent = new Intent(CalendarActivity.this, Booking.class);
                intent.putExtra("date", date);
                startActivity(intent);
//                appointment newAppt = new appointment(date, 14);
//                SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
//                Date currDate = new Date();
//                String theDate = formatter.format(currDate);
//                Toast.makeText(getApplicationContext(), "date " + newAppt.getDate() + "  " + theDate, Toast.LENGTH_SHORT).show();
//                List<appointment> appointmentList = new ArrayList<>();
//                appointmentList.add(newAppt);
            }
        });
    }
}
