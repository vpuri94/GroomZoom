package com.example.groomzoom;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.List;

import es.dmoral.toasty.Toasty;


public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private Context context;
    private List<Browse> browseList;
    public String newLocationKey = "newLocation";
    public String myLocationKey = "myLocation";
    String offerMsg = "Services offered: ";
    String requestMsg = "Services requested: ";
    String mapPointKey = "mapPoint";
    String distanceMsg = "Distance away: ";
    String ellipsis = "...";
    String barberKey = "barber";

    public BrowseAdapter(Context context, List<Browse> browseList) {
        this.context = context;
        this.browseList = browseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_browse, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Browse browse = browseList.get(position);

        try {
            holder.bind(browse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return browseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvService;
        private ImageView ivProfile;
        private RatingBar rbRating;
        private TextView tvDistance;
        private Button btnBook;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvService = itemView.findViewById(R.id.tvService);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            btnBook = itemView.findViewById(R.id.btnBook);
        }

        // binds date from each browse object to views
        public void bind(final Browse browse) throws ParseException {
            tvName.setText(browse.getName());
            String preview = servicePreview(browse.getServices());
            // two different messages differentiated on if barber or not
            if(browse.getBarber())
                tvService.setText(offerMsg + preview);
            else
                tvService.setText(requestMsg + preview);
            ParseFile profilePic = browse.getProfilePic();
            if(profilePic != null)
                Glide.with(context).load(profilePic.getUrl()).into(ivProfile);
            float rating = (float) browse.getRating();
            rbRating.setRating(rating);
            tvDistance.setText(distanceMsg + String.format("%.2f",getDistance(browse)) + "km");
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser.getBoolean(barberKey))
                btnBook.setVisibility(View.GONE);
            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goBook(browse);
                }
            });
        }

        // another string manipulation on displaying a preview of list of services
        public String servicePreview(List<String> services){
            String servPreview = "";
            for(int x = 0; x < services.size() ; x++){
                if(x > 1)
                    return servPreview + ellipsis;
                servPreview += services.get(x) + ", ";
            }
            return servPreview;
        }
    }

    // gets distance from the current user to each user in the browse recyclerView
    public float getDistance(Browse browse) throws ParseException {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint currentLocation = currentUser.fetchIfNeeded().getParseGeoPoint(mapPointKey);
        double myLat = currentLocation.getLatitude();
        double myLong = currentLocation.getLongitude();
        // get longitude and latitude from Parse GeoPoint from Google Maps activity and store as new location object
        Location myLocation = new Location(myLocationKey);
        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLong);

        // get other longitude and latitude from the other user and store as a different location object
        ParseUser eachUser = browse.getAddress();
        ParseGeoPoint newPoint = eachUser.fetchIfNeeded().getParseGeoPoint(mapPointKey);
        double newLat = newPoint.getLatitude();
        double newLong = newPoint.getLongitude();
        Location newLocation = new Location(newLocationKey);
        newLocation.setLongitude(newLong);
        newLocation.setLatitude(newLat);
        // return distance between the two (divide by 1000 to convert m to km)
        return myLocation.distanceTo(newLocation) / 1000;
    }

    private void goBook(Browse browse){
        Intent gotoBookScreen = new Intent(context, Booking.class);
        gotoBookScreen.putExtra("id", browse.getObjectId());
        context.startActivity(gotoBookScreen);
    }


}
