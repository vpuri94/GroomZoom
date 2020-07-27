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
        return inflater.inflate(R.layout.fragment_browse, container, false);
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
        queryBrowse();
    }

    private void queryBrowse() {
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
                allBrowse.addAll(objects);
                browseAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sortBrowse(List<Browse> userList, boolean sortingByRating, boolean sortingByDistance){
        List<Browse> newList = new ArrayList<>();
        Collections.copy(newList, allBrowse);
        if(sortingByRating){
            newList = sortByRating(newList);
        }

        if(sortingByDistance){
            newList = sortByDistance(newList);
        }
    }

    private List<Browse> sortByDistance(List<Browse> newList) {
        ParseGeoPoint currentLocation = currentUser.getParseGeoPoint("mapPoint");
        double myLat = currentLocation.getLatitude();
        double myLong = currentLocation.getLongitude();
        Location myLocation = new Location("myLocation");
        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLong);
        List<Float> distanceList = new ArrayList<>();

        for(Browse browse: newList){
            ParseUser eachUser = browse.getAddress();
            ParseGeoPoint newPoint = eachUser.getParseGeoPoint("mapPoint");
            double newLat = newPoint.getLatitude();
            double newLong = newPoint.getLongitude();
            Location newLocation = new Location("newLocation");
            newLocation.setLongitude(newLong);
            newLocation.setLatitude(newLat);
            distanceList.add(myLocation.distanceTo(newLocation));
        }


        return newList;
    }

    private List<Browse> sortByRating(List<Browse> newList) {
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
        return newList;
    }

}