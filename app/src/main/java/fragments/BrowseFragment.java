package fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    private BrowseAdapter browseAdapter;
    private List<Browse> allBrowse;
    private String TAG = "HI";
    private ParseUser currentUser = ParseUser.getCurrentUser();
    public String[] sortType = {"Sort By: Distance (closest to farthest)", "Sort By: Distance (farthest to closest)", "Sort By: Rating (Highest to Lowest)", "Sort By: Rating (Lowest to Highest)"};
    public  boolean sortingByDistance = false;
    public boolean sortingByRating = false;
    public boolean closestFirst = false;
    public boolean highestFirst = false;

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
        ArrayAdapter<String> adapterSpin = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, sortType);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvUsers = view.findViewById(R.id.rvUsers);
        allBrowse = new ArrayList<>();
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
        if(currentUser.getBoolean("barber"))
            query.whereEqualTo(Browse.KEY_BARBER, false);
        else
            query.whereEqualTo(Browse.KEY_BARBER, true);
        query.findInBackground(new FindCallback<Browse>() {
            @Override
            public void done(List<Browse> objects, ParseException e) {
                if(e != null){
                    return;
                }
                for(Browse browse: objects){
                    Log.i(TAG, "Barbers: " + browse.getName());
                }
                try {
                    objects = sortBrowse(objects, sortingByRating, sortingByDistance);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                allBrowse.addAll(objects);
                browseAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<Browse> sortBrowse(List<Browse> userList, boolean sortingByRating, boolean sortingByDistance) throws ParseException {
        if(sortingByRating){
            userList = sortByRating(userList, highestFirst);
        }

        else if(sortingByDistance){
            userList = sortByDistance(userList, closestFirst);
        }
        return userList;
    }

    private List<Browse> sortByDistance(List<Browse> newList, boolean closestFirst) throws ParseException {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint currentLocation = currentUser.fetchIfNeeded().getParseGeoPoint("mapPoint");
        double myLat = currentLocation.getLatitude();
        double myLong = currentLocation.getLongitude();
        Location myLocation = new Location("myLocation");
        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLong);
        List<Float> distanceList = new ArrayList<>();

        for(Browse browse: newList){
            ParseUser eachUser = browse.getAddress();
            ParseGeoPoint newPoint = eachUser.fetchIfNeeded().getParseGeoPoint("mapPoint");
            double newLat = newPoint.getLatitude();
            double newLong = newPoint.getLongitude();
            Location newLocation = new Location("newLocation");
            newLocation.setLongitude(newLong);
            newLocation.setLatitude(newLat);
            distanceList.add(myLocation.distanceTo(newLocation) / 1000);
        }

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
        if(!closestFirst)
            Collections.reverse(newList);

        return newList;
    }

    private List<Browse> sortByRating(List<Browse> newList, boolean highestFirst) {
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
        if(highestFirst)
            Collections.reverse(newList);
        return newList;
    }

}