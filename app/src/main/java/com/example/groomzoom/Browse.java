package com.example.groomzoom;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

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

public String getName(){
    return getString(KEY_NAME);
}
public void setName(String name){
    put(KEY_NAME, name);
}
public boolean getBarber(){
    return getBoolean(KEY_BARBER);
}
public double getRating(){
    return getDouble(KEY_RATING);
}
public ParseFile getProfilePic(){
    return getParseFile(KEY_PROFILEPIC);
}
    public List<String> getServices(){
        return getList(KEY_SERVICES);
    }

    public ParseUser getAddress() {
        return getParseUser(KEY_ADDRESS);
    }

}
