package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.groomzoom.Maps;
import com.example.groomzoom.Profile;
import com.example.groomzoom.LoginActivity;
import com.example.groomzoom.R;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("User")
public class ProfileFragment extends Fragment {
    protected Button btnSignout;
    private TextView tvMyName;
    private TextView tvAddress;
    private Button btnModifyAddress;
    private ImageView ivPfp;
    private CheckBox cbHaircut;
    private CheckBox cbColor;
    private CheckBox cbBeard;
    private CheckBox cbWax;
    private CheckBox cbBlowdry;
    private Button btnService;
    private ParseUser myself = ParseUser.getCurrentUser();

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignout = view.findViewById(R.id.btnSignout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                goToStartScreen();
            }
        });
        tvMyName = view.findViewById(R.id.tvMyName);
        tvAddress = view.findViewById(R.id.tvAddress);
        btnModifyAddress = view.findViewById(R.id.btnModifyAddress);
        ivPfp = view.findViewById(R.id.ivPfp);

        tvMyName.setText(myself.getUsername());
        tvAddress.setText(myself.getString("address"));
        btnModifyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ADD INTENT TO GOOGLE MAPS PAGE HERE
                modifyAddress();
            }
        });

        ParseFile image = myself.getParseFile("profilePic");
        if (image != null) {
            Glide.with(getContext()).load(image.getUrl()).into(ivPfp);
        }

        cbHaircut = view.findViewById(R.id.cbHaircut);
        cbBeard = view.findViewById(R.id.cbBeard);
        cbColor = view.findViewById(R.id.cbColor);
        cbWax = view.findViewById(R.id.cbWax);
        cbBlowdry = view.findViewById(R.id.cbBlowdry);
        setCheckBoxes();
        btnService = view.findViewById(R.id.btnService);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceChanges(view);
            }
        });
        myself.saveInBackground();
    }

    public void serviceChanges(View v){
        List<String> serviceList = new ArrayList<String>();
        if(cbHaircut.isChecked())
            serviceList.add(String.valueOf(cbHaircut.getText()));
        if(cbBeard.isChecked())
            serviceList.add(String.valueOf(cbBeard.getText()));
        if(cbColor.isChecked())
            serviceList.add(String.valueOf(cbColor.getText()));
        if(cbWax.isChecked())
            serviceList.add(String.valueOf(cbWax.getText()));
        if(cbBlowdry.isChecked())
            serviceList.add(String.valueOf(cbBlowdry.getText()));
        myself.put("services", serviceList);
        Toast.makeText(getContext(), "SAVED CHANGES TO YOUR ACCOUNT", Toast.LENGTH_LONG).show();
        myself.saveInBackground();
    }



    private void goToStartScreen() {
        Intent goToStartScreen = new Intent(this.getContext(), LoginActivity.class);
        startActivity(goToStartScreen);
    }

    private void modifyAddress(){
        Intent googleMaps = new Intent(this.getContext(), Maps.class);
        startActivity(googleMaps);
    }

    private void setCheckBoxes() {
        List<CheckBox> myServices = new ArrayList<CheckBox>();
        myServices.add(cbHaircut);
        myServices.add(cbBeard);
        myServices.add(cbColor);
        myServices.add(cbWax);
        myServices.add(cbBlowdry);

        List<String> myPrefs = myself.getList("services");
        for(int x = 0; x < myServices.size(); x++){
            if(myPrefs.contains(String.valueOf(myServices.get(x).getText()))){
                myServices.get(x).setChecked(true);
            }
        }
    }
}