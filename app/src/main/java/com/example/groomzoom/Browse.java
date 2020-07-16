package com.example.groomzoom;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Browse")
public class Browse extends ParseObject {
public static final String KEY_PROFILEPIC = "profilePic";
public static final String KEY_USERNAME = "username";
public static final String KEY_NAME = "name";
public static final String KEY_BARBER = "barber";
public static final String KEY_RATING = "rating";
public static final String KEY_SERVICES = "services";
public static final String KEY_ADDRESS = "address";

public String getUsername(){
    return getString(KEY_USERNAME);
}
public void setUsername(String username){
    put(KEY_USERNAME, username);
}
public String getName(){
    return getString(KEY_NAME);
}

public void setName(String name){
    put(KEY_NAME, name);
}

public boolean getBarber(){
    return getBoolean(KEY_BARBER);
}
public void setBarber(boolean isBarber){
    put(KEY_BARBER, isBarber);
}
public double getRating(){
    return getDouble(KEY_RATING);
}
public void setRating(double rating){
    put(KEY_RATING, rating);
}
public String getAddress(){
    return getString(KEY_ADDRESS);
}
public void setAddress(String address){
    put(KEY_ADDRESS, address);
}
public ParseFile getProfilePic(){
    return getParseFile(KEY_PROFILEPIC);
}
public void setProfilePic(ParseFile profilePic){
    put(KEY_PROFILEPIC, profilePic);
}
    public List<String> getServices(){
        return getList(KEY_SERVICES);
    }


}
