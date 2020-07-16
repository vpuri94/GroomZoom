package fragments;

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

import com.example.groomzoom.Appointments;
import com.example.groomzoom.AppointmentsAdapter;
import com.example.groomzoom.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentFragment extends Fragment {


public RecyclerView rvAppointments;
public AppointmentsAdapter adapter;
public List<Appointments> allAppointments;
public String[] apptType = {"UPCOMING APPOINTMENTS", "PREVIOUS APPOINTMENTS"};
public boolean upcoming = true;

public static final String TAG = "AppointmentFragment";

    public AppointmentFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_appointment, container, false);
        Spinner spinner = (Spinner)v.findViewById(R.id.apptSpinner);
        ArrayAdapter<String> adapterSpin = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, apptType);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    upcoming = true;
                }
                else{
                    upcoming = false;
                }
                queryAppointments();
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

        rvAppointments = view.findViewById(R.id.rvAppointments);
        allAppointments = new ArrayList<>();
        adapter = new AppointmentsAdapter(getContext(), allAppointments);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        rvAppointments.addItemDecoration(dividerItemDecoration);
        rvAppointments.setAdapter(adapter);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public void queryAppointments() {
        allAppointments.clear();
        ParseQuery<Appointments> query = ParseQuery.getQuery(Appointments.class);
        query.include(Appointments.KEY_USER);
        if(ParseUser.getCurrentUser().getBoolean("barber"))
            query.whereEqualTo(Appointments.KEY_USER, ParseUser.getCurrentUser());
        else
            query.whereEqualTo(Appointments.KEY_BOOKER, ParseUser.getCurrentUser());
        if(upcoming)
            query.whereEqualTo(Appointments.KEY_OCCURRED, false);
        else
            query.whereEqualTo(Appointments.KEY_OCCURRED, true);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Appointments>() {
            @Override
            public void done(List<Appointments> appointments, ParseException e) {
                if(e != null){
                    return;
                }
                for(Appointments appointment : appointments){
                    try {
                        Log.i(TAG, "Appointments: " + appointment.getServices() + ", username: " + appointment.getUser().fetchIfNeeded().getUsername());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
                allAppointments.addAll(appointments);
                adapter.notifyDataSetChanged();

            }
        });
    }

}