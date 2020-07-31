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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

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
    String priceMsg = "price";
    String browseId = "Browse";
    String pfpKey = "profilePic";
    String objectIdKey = "objectId";
    String userRatingKey = "userRating";
    String dateTimeFormat = "M/dd/yyyy HH";
    String addressKey = "address";
    String occurredKey = "occurred";
    String ratingKey = "rating";
    String servicesKey = "services";
    String servicesDoneKey = "servicesDone";
    String bookerKey = "booker";
    String barberProfilePic = "barberProfilePic";
    String userKey = "user";

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

        // add the dates as strings to the backend
        for(int x = 0; x < values2.size(); x++){
            if(date == null){
                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                Date currDate = new Date();
                String theDate = formatter.format(currDate);
                date = theDate;
            }
                values2.set(x, date + values2.get(x));
        }


        // set up view to initially be all available
        for(int x = 0; x < numTimeSlots; x++)
            availability.add(available);



        try {
            // change some rows to be booked depending on backend
            checkBooking(date, objectId);
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
                    Toasty.error(getApplicationContext(), fullMsg, Toasty.LENGTH_SHORT, true).show();
                    return;
                }
                if(position >= 5){
                    try {
                        // set and confirm bookings
                        setBooking(finalDate, position - 4, objectId);
                    } catch (ParseException | java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        setBooking(finalDate, position + 8, objectId);
                    } catch (ParseException | java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
                Toasty.success(getApplicationContext(), bookedMsg, Toasty.LENGTH_SHORT, true).show();
                try {
                    checkBooking(finalDate, objectId);
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
                intent.putExtra(objectIdKey, objectId);
                startActivity(intent);
            }
        });
    }

    private void checkBooking(final String date, String objectId) throws ParseException {
        // get browse object
        ParseQuery<ParseObject> query = ParseQuery.getQuery(browseId);
        ParseObject browse = query.get(objectId);
        ParseFile appts = null;
        appts = browse.getParseFile(apptKey);
        if(appts == null){
            return;
        }

        // get appointment file as string
        byte[] data = appts.getData();
        String text = new String(data);
        int index = text.indexOf(date);
        List<Integer> indicesList = printIndex(text, date);
        if (index == -1) {
            return;
        }
        if (indicesList.size() == 0){
            return;
    }
        // go through and check what times are booked from string value of file
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

    private void setBooking(String date, int time, String objectId) throws ParseException, java.text.ParseException {
        // get data from browse backend
        ParseQuery<ParseObject> query = ParseQuery.getQuery(browseId);
        ParseObject browse = query.get(objectId);
        ParseFile appts = null;
        appts = browse.getParseFile(apptKey);
        String text = "";
        if(appts != null){
            byte[] data = appts.getData();
            text = new String(data);
        }

        // append on a new appt and push it to parse backend
        text = text.concat("\n" + date + " " + time);
        byte[] newData = text.getBytes();
        ParseFile file = new ParseFile(apptFilename, newData);
        file.saveInBackground();
        browse.put(apptKey, file);
        browse.saveInBackground();

        // create new appt in the upcoming appt tab
        ParseObject obj = new Appointments();
        obj.put(priceMsg, browse.getNumber(priceMsg));
        obj.put(occurredKey, false);
        obj.put(userRatingKey, browse.getNumber(ratingKey));
        obj.put(pfpKey, currentUser.getParseFile(pfpKey));
        obj.put(servicesDoneKey, currentUser.getList(servicesKey));
        obj.put(bookerKey, currentUser);
        obj.put(userKey, browse.get(addressKey));
        obj.put(barberProfilePic, browse.getParseUser(addressKey).fetchIfNeeded().get(pfpKey));
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
        Date parsedDate = sdf.parse(date + " " + String.valueOf(time));
        obj.put(dateKey, parsedDate);
        obj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }


    // give back indices of which time slots are booked from the appointments file
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


    // set the list to be booked
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