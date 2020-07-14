package com.example.groomzoom;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@ParseClassName("Appointments")
public class Appointments extends ParseObject {

    public static final String KEY_PROFILEPIC = "profilePic";
    public static final String KEY_BARBER = "barber";
    public static final String KEY_DATE = "date";
    public static final String KEY_PRICE = "price";
    public static final String KEY_OCCURRED = "occurred";
    public static final String KEY_USER = "user";
    public static final String KEY_SERVICE = "service";

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

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date =  getDate(KEY_DATE);
        String dateTime = dateFormat.format(date);
        // Converts the format above into "XX minutes or XX days ago
        return dateTime;

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
    public String getService(){
        return getString(KEY_SERVICE);
    }
    public void setService(String service){
        put(KEY_SERVICE, service);
    }





}
