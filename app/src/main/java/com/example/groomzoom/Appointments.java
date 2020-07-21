package com.example.groomzoom;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ParseClassName("Appointments")
public class Appointments extends ParseObject {

    public static final String KEY_PROFILEPIC = "profilePic";
    public static final String KEY_DATE = "date";
    public static final String KEY_PRICE = "price";
    public static final String KEY_OCCURRED = "occurred";
    public static final String KEY_USER = "user";
    public static final String KEY_SERVICES = "servicesDone";
    public static final String KEY_BOOKER = "booker";
    public static final String KEY_RATING = "userRating";
    public static final String KEY_BARBERPROFILEPIC = "barberProfilePic";

    public ParseFile getProfilePic() {
        return getParseFile(KEY_PROFILEPIC);
    }
    public ParseFile getBarberProfilePic(){ return getParseFile(KEY_BARBERPROFILEPIC); }
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
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
    public int getPrice(){
        return getNumber(KEY_PRICE).intValue();
    }
    public List<String> getServices(){
        return getList(KEY_SERVICES);
    }
    public ParseUser getBooker(){
        return getParseUser(KEY_BOOKER);
    }
    public double getRating(){
        return getDouble(KEY_RATING);
    }
}
