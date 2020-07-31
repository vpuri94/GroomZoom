package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groomzoom.Browse;
import com.example.groomzoom.BrowseAdapter;
import com.example.groomzoom.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class BrowseFragment extends Fragment {


    private RecyclerView rvUsers;
    private TextView tvUserType;
    private BrowseAdapter browseAdapter;
    private List<Browse> allBrowse;
    private String TAG = "HI";
    private ParseUser currentUser = ParseUser.getCurrentUser();
    public String[] sortType = {"Sort By: Distance (closest to farthest)", "Sort By: Distance (farthest to closest)", "Sort By: Rating (Highest to Lowest)", "Sort By: Rating (Lowest to Highest)"};
    public String[] filters = {"No Filter", "Filter By: 4 Stars or more", "Filter By: Less than X Km"};
    public  boolean sortingByDistance = false;
    public boolean sortingByRating = false;
    public boolean closestFirst = false;
    public boolean highestFirst = false;
    public String barberBanner = "Barbers:";
    public String clientBanner = "Clients:";
    public String barberKey = "barber";
    public String mapPointKey = "mapPoint";
    public boolean filterByFour = false;
    public boolean filterByDistance = false;
    public int distanceFilter = 20;
    public String alertTitle = "Enter how many km to filter by";
    public String alertMsg = "Put in the number of kilometers here";
    public String filterButton = "Filter";
    public String cancelButton = "Cancel";
    public String newLocationKey = "newLocation";
    public String myLocationKey = "myLocation";

    public BrowseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_browse, container, false);
        Spinner spinner = (Spinner)v.findViewById(R.id.browseSpinner);
        Spinner spinner2 = (Spinner) v.findViewById(R.id.filterSpinner);

        ArrayAdapter<String> adapterSpin = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, sortType);
        ArrayAdapter<String> adapterSpin2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, filters);


        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // sets up dropdown by rating and distance for sorting by rating
                if(i == 0){
                    sortingByRating = false;
                    highestFirst = false;
                    closestFirst = true;
                    sortingByDistance = true;
                }
                else if(i== 1){
                    sortingByRating = false;
                    highestFirst = false;
                    closestFirst = false;
                    sortingByDistance = true;
                }
                else if(i == 2){
                    closestFirst = false;
                    sortingByDistance = false;
                    sortingByRating = true;
                    highestFirst = true;
                }
                else{
                    closestFirst = false;
                    sortingByDistance = false;
                    sortingByRating = true;
                    highestFirst = false;
                }
                queryBrowse();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

     spinner2.setAdapter(adapterSpin2);

     spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         // filter by distance of X km and 4 star ratings
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             if(i == 0){
                 filterByDistance = false;
                 filterByFour = false;
             }
             else if(i == 1){
                 filterByDistance = false;
                 filterByFour = true;
                 queryBrowse();
             }
             else{
                 filterByFour = false;
                 filterByDistance = true;
                 showAddItemDialog(getContext());
             }
         }

         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {

         }
     });
        return v;
    }

    // show a dialog that allows one to input a certain number of km to filter by
    private void showAddItemDialog(Context c) {

        final EditText taskEditText = new EditText(c);
        // sets it to numeric keyboard only
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c).setTitle(alertTitle).setMessage(alertMsg).setView(taskEditText).setPositiveButton(filterButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                distanceFilter = Integer.valueOf(String.valueOf(taskEditText.getText()));
                queryBrowse();
            }
        }).setNegativeButton(cancelButton, null).create();
        dialog.show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvUsers = view.findViewById(R.id.rvUsers);
        tvUserType = view.findViewById(R.id.tvUserType);
        if(currentUser.getBoolean(barberKey))
            tvUserType.setText(clientBanner);
        else
            tvUserType.setText(barberBanner);
        allBrowse = new ArrayList<>();
        // add dividers for each item to separate out each row in the recyclerview
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rvUsers.addItemDecoration(dividerItemDecoration);
        browseAdapter = new BrowseAdapter(getContext(), allBrowse);
        rvUsers.setAdapter(browseAdapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void queryBrowse() {
        allBrowse.clear();
        ParseQuery<Browse> query = ParseQuery.getQuery(Browse.class);
        query.include(Browse.KEY_USERNAME);
        if(currentUser.getBoolean(barberKey))
            query.whereEqualTo(Browse.KEY_BARBER, false);
        else
            query.whereEqualTo(Browse.KEY_BARBER, true);
        // filter by four star reviews
        if(filterByFour) {
            query.whereGreaterThanOrEqualTo(Browse.KEY_RATING, 4);
        }
        query.findInBackground(new FindCallback<Browse>() {
            @Override
            public void done(List<Browse> objects, ParseException e) {
                if(e != null){
                    return;
                }
                // sort the objects by either rating or distance and put them in the list of browse objects accordingly
                try {
                    objects = sortBrowse(objects, sortingByRating, sortingByDistance);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                if(filterByDistance) {
                    for (Browse browse : objects) {
                        try {
                            if (getDistance(browse) <= distanceFilter) {
                                allBrowse.add(browse);
                            }
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else
                    allBrowse.addAll(objects);
                // let the recyclerview know that the data set has changed
                browseAdapter.notifyDataSetChanged();
            }
        });
    }

    // call helpers for sorting by distance or rating and return the modified list
    private List<Browse> sortBrowse(List<Browse> userList, boolean sortingByRating, boolean sortingByDistance) throws ParseException {
        if(sortingByRating){
            userList = sortByRating(userList, highestFirst);
        }

        else if(sortingByDistance){
            userList = sortByDistance(userList, closestFirst);
        }
        return userList;
    }

    public List<Browse> sortByDistance(List<Browse> newList, boolean closestFirst) throws ParseException {
        // first get current users location and set up a blank distance list
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint currentLocation = currentUser.fetchIfNeeded().getParseGeoPoint(mapPointKey);
        double myLat = currentLocation.getLatitude();
        double myLong = currentLocation.getLongitude();
        Location myLocation = new Location(myLocationKey);
        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLong);
        List<Float> distanceList = new ArrayList<>();

        // then go through each user in the browseList and add the distance the user in browseList is away from currentUser
        for(Browse browse: newList){
            ParseUser eachUser = browse.getAddress();
            ParseGeoPoint newPoint = eachUser.fetchIfNeeded().getParseGeoPoint(mapPointKey);
            double newLat = newPoint.getLatitude();
            double newLong = newPoint.getLongitude();
            Location newLocation = new Location(newLocationKey);
            newLocation.setLongitude(newLong);
            newLocation.setLatitude(newLat);
            distanceList.add(myLocation.distanceTo(newLocation) / 1000);
        }

        // finally, once we have the distance list, go through and sort the distance list accordingly, which we
        // can then use as a key to sort our browse list
        for(int x = 0; x < distanceList.size(); x++){
            for(int y = 0; y < distanceList.size() - 1 - x; y++){
                if(distanceList.get(y) > distanceList.get(y+1)){
                    float temp = distanceList.get(y);
                    distanceList.set(y, distanceList.get(y+1));
                    distanceList.set(y+1, temp);
                    Browse tempBrowse = newList.get(y);
                    newList.set(y, newList.get(y+1));
                    newList.set(y+1, tempBrowse);
                }
            }
        }
        // if we choose to have farthest to closest users instead, we can simply reverse the list
        if(!closestFirst)
            Collections.reverse(newList);

        return newList;
    }

    private List<Browse> sortByRating(List<Browse> newList, boolean highestFirst) {
        // use sort the browse list in order of increasing rating values
        for(int x = 0; x < newList.size(); x++){
            Browse min = newList.get(x);
            int minId = x;
            for(int y = x+1; y < newList.size(); y++){
                if(newList.get(y).getRating() < min.getRating()){
                    min = newList.get(y);
                    minId = y;
                }
            }
            Browse temp = newList.get(x);
            newList.set(x, min);
            newList.set(minId, temp);
        }
        // if we want highest ratings first, then simply reverse the order so its highest to lowest ratings
        if(highestFirst)
            Collections.reverse(newList);
        return newList;
    }

    public float getDistance(Browse browse) throws ParseException {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint currentLocation = currentUser.fetchIfNeeded().getParseGeoPoint(mapPointKey);
        double myLat = currentLocation.getLatitude();
        double myLong = currentLocation.getLongitude();
        Location myLocation = new Location(myLocationKey);
        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLong);

        ParseUser eachUser = browse.getAddress();
        ParseGeoPoint newPoint = eachUser.fetchIfNeeded().getParseGeoPoint(mapPointKey);
        double newLat = newPoint.getLatitude();
        double newLong = newPoint.getLongitude();
        Location newLocation = new Location(newLocationKey);
        newLocation.setLongitude(newLong);
        newLocation.setLatitude(newLat);
        return myLocation.distanceTo(newLocation) / 1000;
    }

}