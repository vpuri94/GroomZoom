package com.example.groomzoom;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("User")
public class Profile extends ParseObject{
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_RATING = "rating";
    public static final String KEY_PFP = "profilePic";
    public static final String KEY_BARBER = "barber";

    public String getUsername(){
        return getString(KEY_USERNAME);
    }
    public void setUsername(String username){
        put(KEY_USERNAME, username);
    }

    public  String getAddress(){
        return getString(KEY_ADDRESS);
    }
    public void setAddress(String address){
        put(KEY_ADDRESS, address);
    }

    public Number getRating() { return getNumber(KEY_RATING); }
    public ParseFile getProfilePic(){
        return getParseFile(KEY_PFP);
    }
    public boolean getBarber() { return getBoolean(KEY_BARBER); }
}
