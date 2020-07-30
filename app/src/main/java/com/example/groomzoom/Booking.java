package com.example.groomzoom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
    int numTimeSlots = 10;
    String available = "Available";
    String booked = "Booked";
    String dateKey = "date";
    String idKey = "id";
    String dateFormat = "M/dd/yyyy";
    String fullMsg = "Sorry! This time slot is full!";
    String bookedMsg = "Booked the appointment!";
    String apptFilename = "appt.txt";
    String apptKey = "appointments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        btnGoCalendar = (Button) findViewById(R.id.btnGoCalendar);
        listview = (ListView) findViewById(R.id.apptList);

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra(dateKey);
        final String objectId = incomingIntent.getStringExtra(idKey);

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
                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                Date currDate = new Date();
                String theDate = formatter.format(currDate);
                date = theDate;
            }
                values2.set(x, date + values2.get(x));
        }


        for(int x = 0; x < numTimeSlots; x++)
            availability.add(available);



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
                if(availability.get(position).equals(booked)){
                    Toast.makeText(getApplicationContext(), fullMsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(position >= 5){
                    try {
                        setBooking(finalDate, position - 4, objectId);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        setBooking(finalDate, position + 8, objectId);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), bookedMsg, Toast.LENGTH_SHORT).show();
                try {
                    checkBooking(finalDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                availability.set(position, booked);
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
        ParseFile appts = (ParseFile) currentUser.get(apptKey);
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
                    int parseInt = Integer.parseInt(String.valueOf(text.charAt(y + 1)));
                    if (y + 2 < text.length()) {
                        if (text.charAt(y + 2) == '\n')
                            timesBooked.add(parseInt);
                        else
                            timesBooked.add(Integer.parseInt(text.charAt(y + 1) + "" + text.charAt(y + 2)));
                        break;
                    }
                    timesBooked.add(parseInt);
                }
            }
        }
    }

    private void setBooking(String date, int time, String username) throws ParseException {
        ParseFile appts = (ParseFile) currentUser.get(apptKey);
        byte[] data = appts.getData();
        String text = new String(data);
        text = text.concat("\n" + date + " " + time);
        byte[] newData = text.getBytes();
        ParseFile file = new ParseFile(apptFilename, newData);
        file.saveInBackground();
        currentUser.put(apptKey, file);
        currentUser.saveInBackground();

//        ParseObject obj = ParseObject.create("Appointments");
//        obj.put("price", 25);
//        obj.put("occurred", false);
//        obj.put("userRating", 5);
//        obj.saveInBackground();
//        ParseQuery<ParseUser> query = ParseQuery.getQuery("User");
//
//        query.getInBackground(username, new GetCallback<ParseUser>() {
//            @Override
//            public void done(ParseUser object, ParseException e) {
//                if (e == null) {
//                    Toast.makeText(getApplicationContext(), "SAVED", Toast.LENGTH_SHORT).show();
//                    ParseObject newAppt = ParseObject.create("Appointments");
//                    newAppt.put("occurred", false);
//                     hard coded need to change
//                    newAppt.put("price", 25);
//                    newAppt.put("userRating", object.getNumber("rating"));
//                    newAppt.put("profilePic", object.getParseFile("frontSelfie"));
//                    newAppt.saveInBackground();
//                }
//            }
//        });


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
            return indices;
        }
        return indices;
    }


    private void changeAvailability(ArrayList<Integer> timesBooked){
        for(int x = 0; x < timesBooked.size(); x++){
            if(timesBooked.get(x) >= 8){
                availability.set(timesBooked.get(x) - 8, booked);
            }
            else{
                availability.set(timesBooked.get(x) + 4, booked);
            }
        }
    }
}