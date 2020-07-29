package com.example.groomzoom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Booking extends AppCompatActivity {
    ParseUser currentUser = ParseUser.getCurrentUser();
    public static final String TAG = "booking";

    private Button btnGoCalendar;
    private ListView listview;
    private BookingAdapter adapter;

    ArrayList<String> values2 = new ArrayList<String>();
    ArrayList<String> availability = new ArrayList<String>();
    ArrayList<Integer> timesBooked = new ArrayList<Integer>();


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        btnGoCalendar = (Button) findViewById(R.id.btnGoCalendar);
        listview = (ListView) findViewById(R.id.apptList);

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        values2.add(" @ 8AM");
        values2.add(" @ 9AM");
        values2.add(" @ 10AM");
        values2.add(" @ 11AM");
        values2.add(" @ 12PM");
        values2.add(" @ 1PM");
        values2.add(" @ 2PM");
        values2.add(" @ 3PM");
        values2.add(" @ 4PM");
        values2.add(" @ 5PM");
        for(int x = 0; x < values2.size(); x++){
            if(date == null){
                SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
                Date currDate = new Date();
                String theDate = formatter.format(currDate);
                date = theDate;
            }
                values2.set(x, date + values2.get(x));
        }




        availability.add("Available");
        availability.add("Available");
        availability.add("Available");
        availability.add("Available");
        availability.add("Available");
        availability.add("Available");
        availability.add("Available");
        availability.add("Available");
        availability.add("Available");
        availability.add("Available");

        try {
            checkBooking(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        adapter = new BookingAdapter(this, values2, availability);

        listview.setAdapter(adapter);

        final String finalDate = date;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position >= 5){
                    try {
                        setBooking(finalDate, position - 4);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        setBooking(finalDate, position + 8);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), "Booked the appointment!", Toast.LENGTH_SHORT).show();
                try {
                    checkBooking(finalDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                availability.set(position, "Booked");
                adapter.notifyDataSetChanged();
            }
        });

        changeAvailability(timesBooked);



        btnGoCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(Booking.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkBooking(final String date) throws ParseException {
        ParseFile appts = (ParseFile) currentUser.get("appointments");
        byte[] data = appts.getData();
        String text = new String(data);
        int index = text.indexOf(date);
        List<Integer> indicesList = printIndex(text, date);
        if(index == -1)
            return;
        if(indicesList.size() == 0)
            return;
        for(int x = 0; x < indicesList.size(); x++){
            int currIndex = indicesList.get(x);
            int nextIndex;
            if(x == indicesList.size() - 1)
                nextIndex = text.length();
            else{
                nextIndex = indicesList.get(x+1);
            }
            for(int y = currIndex; y < nextIndex; y++) {
                if (text.charAt(y) == ' ') {
                    if (y + 2 < text.length()) {
                        if (text.charAt(y + 2) == '\n')
                            timesBooked.add(Integer.parseInt(String.valueOf(text.charAt(y + 1))));
                        else
                            timesBooked.add(Integer.parseInt(text.charAt(y + 1) + "" + text.charAt(y + 2)));
                        break;
                    }
                    timesBooked.add(Integer.parseInt(String.valueOf(text.charAt(y + 1))));
                }
            }
        }
    }

    private void setBooking(String date, int time) throws ParseException {
        ParseFile appts = (ParseFile) currentUser.get("appointments");
        byte[] data = appts.getData();
        String text = new String(data);
        text = text.concat("\n" + date + " " + time);
        byte[] newData = text.getBytes();
        ParseFile file = new ParseFile("appt.txt", newData);
        file.saveInBackground();
        currentUser.put("appointments", file);
        currentUser.saveInBackground();
    }


    static List<Integer> printIndex(String str, String s)
    {
        List<Integer> indices = new ArrayList<>();

        boolean flag = false;
        for (int i = 0; i < str.length() - s.length() + 1; i++) {
            if (str.substring(i, i + s.length()).equals(s)) {
                indices.add(i);
                flag = true;
            }
        }

        if (flag == false) {
            System.out.println("NONE");
            return indices;
        }
        return indices;
    }


    private void changeAvailability(ArrayList<Integer> timesBooked){
        for(int x = 0; x < timesBooked.size(); x++){
            if(timesBooked.get(x) >= 8){
                Log.i("message", "before 8 " + timesBooked.get(x));
                availability.set(timesBooked.get(x) - 8, "Booked");
            }
            else{
                Log.i("message", "after 8 " + timesBooked.get(x));
                availability.set(timesBooked.get(x) + 4, "Booked");
            }
        }
    }
}