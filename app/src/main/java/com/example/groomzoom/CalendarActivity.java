package com.example.groomzoom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
private CalendarView mCalendarView;

public class appointment{
    public String date;
    public int time;
    public boolean available;
    public appointment(String date, int time, boolean available){
        this.date = date;
        this.time = time;
        this.available = available;
    }

    public String getDate(){
        return date;
    }
}

public class calendar {
public appointment[] appointments;
public calendar(appointment[] appointments){
    this.appointments = appointments;
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
                appointment newAppt = new appointment(date, 14, true);
//                Toast.makeText(getApplicationContext(), "date " + newAppt.getDate(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
