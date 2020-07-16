package com.example.groomzoom;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Appointments")
public class Appointments extends ParseObject {

    public static final String KEY_PROFILEPIC = "profilePic";
    public static final String KEY_BARBER = "barber";
    public static final String KEY_DATE = "date";
    public static final String KEY_PRICE = "price";
    public static final String KEY_OCCURRED = "occurred";
    public static final String KEY_USER = "user";
    public static final String KEY_SERVICES = "servicesDone";
    public static final String KEY_BOOKER = "booker";
    public static final String KEY_RATING = "userRating";


    public ParseFile getProfilePic() {
        return getParseFile(KEY_PROFILEPIC);
    }

    public void setProfilePic(ParseFile parseFile) {
        put(KEY_PROFILEPIC, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public boolean isBarber(){
        return getBoolean(KEY_BARBER);
    }
    public void setBarber(boolean isBarber){
        put(KEY_BARBER, isBarber);
    }

    public String[] getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date date =  getDate(KEY_DATE);
        String dateTime = dateFormat.format(date);
        String time = timeFormat.format(date);
        String[] dateTimeFinal = {dateTime, time};
        // Converts the format above into "XX minutes or XX days ago
        return dateTimeFinal;

    }

    public void setDate(Date date){
        put(KEY_DATE, date);
    }
    public int getPrice(){
        return getNumber(KEY_PRICE).intValue();
    }
    public void setPrice(Number price){
        put(KEY_PRICE, price);
    }
    public boolean getOccurred(){
        return getBoolean(KEY_OCCURRED);
    }
    public void setOccurred(boolean occurred){
        put(KEY_OCCURRED, occurred);
    }
    public List<String> getServices(){
        return getList(KEY_SERVICES);
    }

    public String getBooker(){
        return getString(KEY_BOOKER);
    }
    public void setBooker(String booker){
        put(KEY_BOOKER, booker);
    }

    public double getRating(){
        return getDouble(KEY_RATING);
    }
    public void setRating(double rating){
        put(KEY_RATING, rating);
    }



}
