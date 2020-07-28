package com.example.groomzoom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fragments.BrowseFragment;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private Context context;
    private List<Browse> browseList;
    String offerMsg = "Services offered: ";
    String requestMsg = "Services requested: ";
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

        public void bind(Browse browse) throws ParseException {
            tvName.setText(browse.getName());
            String preview = servicePreview(browse.getServices());
            if(browse.getBarber())
                tvService.setText(offerMsg + preview);
            else
                tvService.setText(requestMsg + preview);
            ParseFile profilePic = browse.getProfilePic();
            if(profilePic != null)
                Glide.with(context).load(profilePic.getUrl()).into(ivProfile);
            float rating = (float) browse.getRating();
            rbRating.setRating(rating);
            tvDistance.setText("Distance away: "+ String.format("%.2f",getDistance(browse)) + "km");
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(!currentUser.getBoolean("barber"))
                btnBook.setVisibility(View.GONE);
            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goBook();
                }
            });
        }

        public String servicePreview(List<String> services){
            String servPreview = "";
            String ending = ".....";
            for(int x = 0; x < services.size() ; x++){
                if(x > 1)
                    return servPreview + ending;
                servPreview += services.get(x) + ", ";
            }
            return servPreview;
        }
    }

    public float getDistance(Browse browse) throws ParseException {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseGeoPoint currentLocation = currentUser.fetchIfNeeded().getParseGeoPoint("mapPoint");
        double myLat = currentLocation.getLatitude();
        double myLong = currentLocation.getLongitude();
        Location myLocation = new Location("myLocation");
        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLong);

        ParseUser eachUser = browse.getAddress();
        ParseGeoPoint newPoint = eachUser.fetchIfNeeded().getParseGeoPoint("mapPoint");
        double newLat = newPoint.getLatitude();
        double newLong = newPoint.getLongitude();
        Location newLocation = new Location("newLocation");
        newLocation.setLongitude(newLong);
        newLocation.setLatitude(newLat);
        return myLocation.distanceTo(newLocation) / 1000;
    }

    private void goBook(){
        Intent gotoBookScreen = new Intent(context, Booking.class);
        context.startActivity(gotoBookScreen);
    }


}
