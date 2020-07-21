package com.example.groomzoom;

import android.content.Context;
import android.content.Intent;
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
import com.parse.ParseUser;
import org.parceler.Parcels;
import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {


    private Context context;
    private List<Appointments> appointments;
    String costMsg = "Cost: $";
    String requestMsg = "Service requested:  ";
    String wantMsg = "Services you wanted: ";
    String barberKey = "barber";

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

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

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
            itemView.setOnClickListener(this);
        }

        public void bind(Appointments appointment) throws ParseException {
            // Bind the post data to the view elements
            boolean isBarber;
            isBarber = ParseUser.getCurrentUser().getBoolean(barberKey);
            if(isBarber) {
                tvUsername.setText(AppointmentDetailsActivity.appointmentMsg + appointment.getBooker().fetchIfNeeded().getUsername());
                tvService.setText(requestMsg + servicePreview(appointment.getServices()));
            }
            else{
                tvUsername.setText(AppointmentDetailsActivity.appointmentMsg + appointment.getUser().fetchIfNeeded().getUsername());
                tvService.setText(wantMsg + servicePreview(appointment.getServices()));
            }
            tvDate.setText(AppointmentDetailsActivity.dateMsg + appointment.getDate()[0] + " @ "+ appointment.getDate()[1]);
            tvPrice.setText(costMsg + Integer.toString(appointment.getPrice()));
            ParseFile image;
            if(isBarber)
                image = appointment.getProfilePic();
            else
                image = appointment.getBarberProfilePic();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivProfilePic);
            }
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position  = getAdapterPosition();
            // make sure the position is valid, i.e actually exists in the view
            if(position != RecyclerView.NO_POSITION){
                // get the movie at the position, this wont work if the class is static
                Appointments appointment = appointments.get(position);
                // Create intent for new activity
                Intent intent = new Intent(context, AppointmentDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Appointments.class.getSimpleName(), Parcels.wrap(appointment));
                // show the activity
                context.startActivity(intent);
            }
        }

        public String servicePreview(List<String> services){
            String servPreview = "";
            for(int x = 0; x < services.size() ; x++){
                if(x > 1)
                    break;
                servPreview += services.get(x) + ", ";
            }
            return servPreview + "...";
        }
    }
}
