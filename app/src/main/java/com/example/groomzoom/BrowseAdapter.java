package com.example.groomzoom;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

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

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvService = itemView.findViewById(R.id.tvService);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            rbRating = itemView.findViewById(R.id.rbRating);
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
        private List<Browse> sortByDistance(List<Browse> newList) throws ParseException {
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
                        newList.set(y, newList.get(y+1));
                        newList.set(y+1, newList.get(y));
                    }
                }
            }
            for(Float dist: distanceList){
                Toast.makeText(context, "dist "+ dist, Toast.LENGTH_SHORT).show();
            }

            return newList;
        }


    }
}
