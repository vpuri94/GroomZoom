package com.example.groomzoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private Context context;
    private List<Appointments> appointments;
    private String TAG = "AppointmentsAdapter";

    public AppointmentsAdapter(Context context, List<Appointments> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointments appointment = appointments.get(position);
        try {
            holder.bind(appointment);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{

        private TextView tvUsername;
        private TextView tvDate;
        private TextView tvService;
        private TextView tvPrice;
        private ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvService = itemView.findViewById(R.id.tvService);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }

        public void bind(Appointments appointment) throws ParseException {
            // Bind the post data to the view elements
            tvUsername.setText(appointment.getUser().fetchIfNeeded().getUsername());
            tvDate.setText(appointment.getDate());
            tvService.setText(appointment.getService());
            tvPrice.setText("Price: $" + Integer.toString(appointment.getPrice()));
            ParseFile image = appointment.getProfilePic();
            if (image != null) {
                Glide.with(context).load(appointment.getProfilePic().getUrl()).into(ivProfilePic);
            }

        }
    }

}
