package com.example.groomzoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private Context context;
    private List<Browse> browseList;

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
        holder.bind(browse);
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

        public void bind(Browse browse) {
            tvName.setText(browse.getName());
            tvService.setText("HAIRCUT AND COLOR PLACEHOLDER");
            ParseFile profilePic = browse.getProfilePic();
            if(profilePic != null)
                Glide.with(context).load(profilePic.getUrl()).into(ivProfile);
            float rating = (float) browse.getRating();
            rbRating.setRating(rating = rating > 0 ? rating / 2.0f : rating);

        }
    }
}
